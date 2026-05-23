package uo.ri.cws.application.service.payroll.crud.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollAssembler;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class GenerateForPreviousMonthOf implements Command<List<PayrollDto>> {

    private PayrollGateway pg = Factories.persistence.forPayroll();
    private LocalDate present;

    public GenerateForPreviousMonthOf(LocalDate present) {
        ArgumentChecks.isNotNull(present, "present cannot be null");
        this.present = present;
    }

    @Override
    public List<PayrollDto> execute() throws BusinessException {
        if (pg.alreadyGeneratedForPrevMonthof(present)) {
            return new ArrayList<>();
        }
        List<PayrollRecord> payrolls = pg.generateForPrevMonthof(present);
        return PayrollAssembler.toDtoList(payrolls);
    }

}