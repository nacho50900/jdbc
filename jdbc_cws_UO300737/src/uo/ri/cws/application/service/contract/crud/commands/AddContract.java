package uo.ri.cws.application.service.contract.crud.commands;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContract implements Command<ContractDto> {

    private final ContractDto dto;
    private final ContractGateway cg  = Factories.persistence.forContract();
    private final MechanicGateway mg  = Factories.persistence.forMechanic();
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();
    private final ProfessionalGroupGateway pgg =
            Factories.persistence.forProfessionalGroup();
    private final PayrollGateway pg =
            Factories.persistence.forPayroll();

    public AddContract(ContractDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractDto cannot be null");
        ArgumentChecks.isNotBlank(dto.mechanic.nif,
                "Mechanic NIF cannot be blank");
        ArgumentChecks.isNotBlank(dto.contractType.name,
                "Contract type name cannot be blank");
        ArgumentChecks.isNotBlank(dto.professionalGroup.name,
                "Professional group name cannot be blank");
        ArgumentChecks.isTrue(dto.annualBaseSalary > 0,
                "Annual base salary must be > 0");
        this.dto = dto;
    }

    @Override
    public ContractDto execute() throws BusinessException {

        // 1. Resolve mechanic
        Optional<MechanicRecord> mechanic =
                mg.findByNif(dto.mechanic.nif);
        BusinessChecks.exists(mechanic, "Mechanic does not exist");

        // 2. Resolve contract type
        Optional<ContractTypeRecord> contractType =
                ctg.findByName(dto.contractType.name);
        BusinessChecks.exists(contractType, "Contract type does not exist");

        // 3. Resolve professional group
        Optional<ProfessionalGroupRecord> profGroup =
                pgg.findByName(dto.professionalGroup.name);
        BusinessChecks.exists(profGroup,
                "Professional group does not exist");

        // 4. FIXED_TERM requires an end date later than start date
        LocalDate startDate =
                LocalDate.now().plusMonths(1).withDayOfMonth(1);

        if ("FIXED_TERM".equals(contractType.get().name)) {
            ArgumentChecks.isNotNull(dto.endDate,
                    "End date is required for FIXED_TERM contracts");
            if (!dto.endDate.isAfter(startDate)) {
                throw new BusinessException(
                        "End date must be after start date");
            }
        }

        // 5. Terminate current active contract if any
        Optional<ContractRecord> active = cg.findInForceByMechanicId(mechanic.get().id);
        if (active.isPresent()) {
            terminatePrevious(active.get(), contractType.get());
        }

        // 6. Create new contract
        ContractRecord newRecord = new ContractRecord();
        newRecord.id = UUID.randomUUID().toString();
        newRecord.version = 1L;
        newRecord.mechanicId = mechanic.get().id;
        newRecord.contractTypeId = contractType.get().id;
        newRecord.professionalGroupId = profGroup.get().id;
        newRecord.startDate = startDate;
        newRecord.endDate = dto.endDate;
        newRecord.annualBaseSalary = dto.annualBaseSalary;
        newRecord.taxRate = computeTaxRate(dto.annualBaseSalary);
        newRecord.settlement = 0.0;
        newRecord.state = "IN_FORCE";

        cg.add(newRecord);

        // 7. Fill and return dto
        dto.id = newRecord.id;
        dto.version = newRecord.version;
        dto.mechanic.id = mechanic.get().id;
        dto.contractType.id = contractType.get().id;
        dto.contractType.compensationDaysPerYear =
                contractType.get().compensationDays;
        dto.professionalGroup.id = profGroup.get().id;
        dto.startDate = newRecord.startDate;
        dto.annualBaseSalary = newRecord.annualBaseSalary;
        dto.taxRate = newRecord.taxRate;
        dto.settlement = 0.0;
        dto.state = "IN_FORCE";
        return dto;
    }

    private void terminatePrevious(ContractRecord old,
            ContractTypeRecord type) throws BusinessException {

        old.endDate = lastDayOfCurrentMonth();
        old.state = "TERMINATED";

        long daysElapsed = ChronoUnit.DAYS.between(
                old.startDate, old.endDate);

        if (daysElapsed >= 365) {
            long fullYears = ChronoUnit.YEARS.between(
                    old.startDate, old.endDate);

            List<PayrollRecord> payrolls = pg.findByContractId(old.id);
            double sumGross = payrolls.stream()
                    .limit(12)
                    .mapToDouble(p -> p.baseSalary + p.extraSalary
                            + p.productivityEarning + p.trienniumEarning)
                    .sum();

            old.settlement = (sumGross / 365.0)
                    * type.compensationDays
                    * fullYears;
        } else {
            old.settlement = 0.0;
        }

        cg.update(old);
    }

    private LocalDate lastDayOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }

    /**
     * Computes the income tax rate from annual base salary brackets
     * as defined in the problem statement.
     */
    private double computeTaxRate(double annualSalary) {
        if (annualSalary <= 12_450) {
            return 0.19;
        } else if (annualSalary < 20_200) {
            return 0.24;
        } else if (annualSalary < 35_200) {
            return 0.30;
        } else if (annualSalary < 60_000) {
            return 0.37;
        } else if (annualSalary < 300_000) {
            return 0.45;
        } else {
            return 0.47;
        }
    }

}
