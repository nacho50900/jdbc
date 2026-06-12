package uo.ri.cws.application.service.payroll.crud.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.intervention.InterventionGateway.InterventionRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkorderRecord;
import uo.ri.cws.application.service.payroll.PayrollAssembler;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class GenerateForPreviousMonthOf implements Command<List<PayrollDto>> {

    private final LocalDate presentDate;

    private final ContractGateway cg = Factories.persistence.forContract();
    private final PayrollGateway pg = Factories.persistence.forPayroll();
    private final ProfessionalGroupGateway pgg =
	Factories.persistence.forProfessionalGroup();
    private final InterventionGateway ig =
	Factories.persistence.forIntervention();
    private final WorkOrderGateway wog = Factories.persistence.forWorkOrder();

    public GenerateForPreviousMonthOf(LocalDate presentDate) {
	ArgumentChecks.isNotNull(presentDate, "presentDate cannot be null");
	this.presentDate = presentDate;
    }

    @Override
    public List<PayrollDto> execute() throws BusinessException {
	YearMonth targetMonth = YearMonth.from(presentDate).minusMonths(1);
	LocalDate firstOfTarget = targetMonth.atDay(1);
	LocalDate lastOfTarget = targetMonth.atEndOfMonth();

	List<PayrollDto> generated = new ArrayList<>();

	for (ContractRecord contract : cg.findAll()) {
	    if (!isEligible(contract, firstOfTarget, lastOfTarget)) {
		continue;
	    }

	    boolean alreadyGenerated = pg.findByContractId(contract.id)
		.stream()
		.anyMatch(p -> YearMonth.from(p.date).equals(targetMonth));
	    if (alreadyGenerated) {
		continue;
	    }

	    ProfessionalGroupRecord group =
		pgg.findById(contract.professionalGroupId)
		   .orElseThrow(() -> new BusinessException(
		       "Professional group not found: "
			   + contract.professionalGroupId));

	    PayrollRecord payroll = computePayroll(
		contract, group, targetMonth, lastOfTarget);

	    pg.add(payroll);
	    generated.add(PayrollAssembler.toDto(payroll));
	}

	return generated;
    }

    private boolean isEligible(ContractRecord c,
            LocalDate firstOfTarget, LocalDate lastOfTarget) {
        if (c.startDate.isAfter(lastOfTarget)) {
            return false;
        }
        if (c.endDate != null 
                && c.endDate.isBefore(firstOfTarget)
                && !"IN_FORCE".equalsIgnoreCase(c.state)) {
            return false;
        }
        return true;
    }

    private PayrollRecord computePayroll(ContractRecord contract,
	    ProfessionalGroupRecord group,
	    YearMonth targetMonth,
	    LocalDate payrollDate) {
	PayrollRecord p = new PayrollRecord();
	p.id = UUID.randomUUID().toString();
	p.version = 1L;
	p.contractId = contract.id;
	p.date = payrollDate;

	p.baseSalary = round3(computeBaseSalary(contract));
	p.extraSalary = round3(computeExtraSalary(p.baseSalary, targetMonth));
	p.productivityEarning = round3(
	    computeProductivity(contract, group, targetMonth));
	p.trienniumEarning = round3(
	    computeTriennium(contract, group, payrollDate));

	p.grossSalary = p.baseSalary + p.extraSalary
	    + p.productivityEarning + p.trienniumEarning;

	p.taxDeduction = round3(computeTax(contract, p.grossSalary));
	p.nicDeduction = round3(computeNic(contract));

	p.totalDeductions = p.taxDeduction + p.nicDeduction;
	p.netSalary = round2(p.grossSalary - p.totalDeductions);

	return p;
    }

    private double computeBaseSalary(ContractRecord contract) {
	return contract.annualBaseSalary / 14.0;
    }

    private double computeExtraSalary(double baseSalary, YearMonth targetMonth) {
	int month = targetMonth.getMonthValue();
	return (month == 6 || month == 12) ? baseSalary : 0.0;
    }

    private double computeProductivity(ContractRecord contract,
        ProfessionalGroupRecord group, YearMonth targetMonth) {

    LocalDateTime first = targetMonth.atDay(1).atStartOfDay();
    LocalDateTime last  = targetMonth.atEndOfMonth().atTime(23, 59, 59);

    List<InterventionRecord> interventions =
        ig.findByMechanicId(contract.mechanicId);

    double total = 0.0; 
    for (InterventionRecord intervention : interventions) {
        Optional<WorkorderRecord> owo = wog.findById(intervention.workOrderId);
        if (owo.isEmpty()) {
            continue;
        }
        WorkorderRecord wo = owo.get();
        if (!"INVOICED".equalsIgnoreCase(wo.state)) {
            continue;
        }

        LocalDateTime dateToCheck = intervention.date != null ? intervention.date : wo.date;

        if (dateToCheck == null) {
            continue;
        }
        if (dateToCheck.isBefore(first) || dateToCheck.isAfter(last)) {
            continue;
        }
        total += wo.amount;
    }

    //System.out.println("total before rate: " + total);
    //System.out.println("productivityRate: " + group.productivityRate);
    //System.out.println("result: " + total * (group.productivityRate / 100.0));

    return total * group.productivityRate;
    }

    private double computeTriennium(ContractRecord contract,
	    ProfessionalGroupRecord group, LocalDate payrollDate) {
	long fullYears = ChronoUnit.YEARS.between(
	    contract.startDate, payrollDate);
	long triennia = fullYears / 3;
	return triennia * group.trienniumPayment;
    }

    private double computeTax(ContractRecord contract, double grossSalary) {
	return grossSalary * getTaxRate(contract.annualBaseSalary);
    }

    private double getTaxRate(double annualBaseSalary) {
	if (annualBaseSalary <= 12_450) {
		return 0.19;
	}
	if (annualBaseSalary <= 20_200) {
		return 0.24;
	}
	if (annualBaseSalary <= 35_200) {
		return 0.30;
	}
	if (annualBaseSalary <= 60_000) {
		return 0.37;
	}
	if (annualBaseSalary <= 300_000) {
		return 0.45;
	}
	return 0.47;
    }

    private double computeNic(ContractRecord contract) {
	return (contract.annualBaseSalary * 0.05) / 12.0;
    }

    private double round3(double value) {
	return Math.round(value * 1_000.0) / 1_000.0;
    }

    private double round2(double value) {
	return Math.round(value * 100.0) / 100.0;
    }

}