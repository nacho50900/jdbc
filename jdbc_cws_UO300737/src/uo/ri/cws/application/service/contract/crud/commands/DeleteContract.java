package uo.ri.cws.application.service.contract.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContract implements Command<Void> {

    private final String id;
    private final ContractGateway cg = Factories.persistence.forContract();
    private final MechanicGateway mg = Factories.persistence.forMechanic();

    public DeleteContract(String id) {
        ArgumentChecks.isNotNull(id, "Contract id cannot be null");
        ArgumentChecks.isNotBlank(id, "Contract id cannot be blank");
        this.id = id;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractRecord> existing = cg.findById(id);
        BusinessChecks.exists(existing, "Contract does not exist");

        ContractRecord contract = existing.get();

        if (cg.mechanicHasWorkOrders(contract.mechanicId)) {
            throw new BusinessException(
                    "Cannot delete contract: mechanic has work orders");
        }
        if (mg.hasInterventions(contract.mechanicId)) {
            throw new BusinessException(
                    "Cannot delete contract: mechanic has interventions");
        }
        if (cg.hasPayrolls(id)) {
            throw new BusinessException(
                    "Cannot delete contract: payrolls already generated");
        }

        cg.remove(id);
        return null;
    }

}
