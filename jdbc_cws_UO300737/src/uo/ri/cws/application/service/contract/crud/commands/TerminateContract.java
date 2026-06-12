package uo.ri.cws.application.service.contract.crud.commands;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class TerminateContract implements Command<Void> {

    private final String contractId;
    private final ContractGateway cg  = Factories.persistence.forContract();
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();
    private final PayrollGateway pg =
            Factories.persistence.forPayroll();

    public TerminateContract(String contractId) {
        ArgumentChecks.isNotNull(contractId, "Contract id cannot be null");
        ArgumentChecks.isNotBlank(contractId,
                "Contract id cannot be blank");
        this.contractId = contractId;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractRecord> existing = cg.findById(contractId);
        BusinessChecks.exists(existing, "Contract does not exist");

        ContractRecord contract = existing.get();
        if (!"IN_FORCE".equals(contract.state)) {
            throw new BusinessException(
                    "Only IN_FORCE contracts can be terminated");
        }

        Optional<ContractTypeRecord> optType =
                ctg.findById(contract.contractTypeId);
        BusinessChecks.exists(optType, "Contract type not found");
        ContractTypeRecord type = optType.get();

        contract.endDate = lastDayOfCurrentMonth();
        contract.state = "TERMINATED";

        long daysElapsed = ChronoUnit.DAYS.between(
                contract.startDate, contract.endDate);

        if (daysElapsed >= 365) {
            long fullYears = ChronoUnit.YEARS.between(
                    contract.startDate, contract.endDate);

            List<PayrollRecord> payrolls = pg.findByContractId(contract.id);
            double sumGross = payrolls.stream()
                    .limit(12)
                    .mapToDouble(p -> p.baseSalary + p.extraSalary
                            + p.productivityEarning + p.trienniumEarning)
                    .sum();

            contract.settlement = (sumGross / 365.0)
                    * type.compensationDays
                    * fullYears;
        } else {
            contract.settlement = 0.0;
        }

        cg.update(contract);
        return null;
    }

    
    private LocalDate lastDayOfCurrentMonth() {
        LocalDate now = LocalDate.now();
        return now.withDayOfMonth(now.lengthOfMonth());
    }

}
