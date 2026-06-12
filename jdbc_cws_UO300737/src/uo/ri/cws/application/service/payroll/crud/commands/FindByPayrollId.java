package uo.ri.cws.application.service.payroll.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollAssembler;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindByPayrollId implements Command<Optional<PayrollDto>> {

    private String id;
    private PayrollGateway pg = Factories.persistence.forPayroll();

    public FindByPayrollId(String id) {
        ArgumentChecks.isNotNull(id, "id cannot be null");
        ArgumentChecks.isNotBlank(id, "id cannot be blank");
        this.id = id;
    }

    @Override
    public Optional<PayrollDto> execute() throws BusinessException {
        Optional<PayrollRecord> op = pg.findById(id);
        if (op.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(PayrollAssembler.toDto(op.get()));
    }

}