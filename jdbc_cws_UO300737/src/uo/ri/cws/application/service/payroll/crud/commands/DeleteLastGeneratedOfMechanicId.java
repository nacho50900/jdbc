package uo.ri.cws.application.service.payroll.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteLastGeneratedOfMechanicId implements Command<Integer> {

    private PayrollGateway  pg = Factories.persistence.forPayroll();
    private MechanicGateway mg = Factories.persistence.forMechanic();
    private String mechanicId;

    public DeleteLastGeneratedOfMechanicId(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId, "mechanicId cannot be null");
        ArgumentChecks.isNotBlank(mechanicId, "mechanicId cannot be blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public Integer execute() throws BusinessException {
        Optional<MechanicRecord> om = mg.findById(mechanicId);
        BusinessChecks.exists(om, "The mechanic does not exist");

        return pg.deleteLastGeneratedByMechanicId(mechanicId);
    }

}