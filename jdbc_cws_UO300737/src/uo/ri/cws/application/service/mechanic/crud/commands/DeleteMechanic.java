package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteMechanic implements Command<Void> {

    private String mechanicId;
    private MechanicGateway mg = Factories.persistence.forMechanic();

    public DeleteMechanic(String mechanicId) {
        ArgumentChecks.isNotNull(mechanicId, "Mechanic id cannot be null");
        ArgumentChecks.isNotBlank(mechanicId, "Mechanic id cannot be blank");
        this.mechanicId = mechanicId;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<MechanicRecord> om = mg.findById(mechanicId);
        BusinessChecks.exists(om, "The mechanic does not exist");

        checkNoWorkorders();
        checkNoInterventions();
        checkNoContract();

        mg.remove(mechanicId);
        return null;
    }

    private void checkNoWorkorders() throws BusinessException {
        if (mg.hasWorkOrders(mechanicId)) {
            throw new BusinessException(
                    "Cannot delete a mechanic with work orders assigned");
        }
    }

    private void checkNoInterventions() throws BusinessException {
        if (mg.hasInterventions(mechanicId)) {
            throw new BusinessException(
                    "Cannot delete a mechanic with interventions assigned");
        }
    }

    private void checkNoContract() throws BusinessException {
        if (mg.hasContracts(mechanicId)) {
            throw new BusinessException(
                    "Cannot delete a mechanic with contracts assigned");
        }
    }

}