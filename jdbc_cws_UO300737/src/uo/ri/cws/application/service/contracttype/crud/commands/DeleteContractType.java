package uo.ri.cws.application.service.contracttype.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class DeleteContractType implements Command<Void> {

    private final String name;
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    public DeleteContractType(String name) {
        ArgumentChecks.isNotNull(name, "Name cannot be null");
        ArgumentChecks.isNotBlank(name, "Name cannot be blank");
        this.name = name;
    }

    @Override
    public Void execute() throws BusinessException {
        Optional<ContractTypeRecord> existing = ctg.findByName(name);
        BusinessChecks.exists(existing, "Contract type does not exist");

        int count = ctg.countContractsFor(existing.get().id);
        if (count > 0) {
            throw new BusinessException(
                    "Cannot delete a contract type with contracts assigned");
        }

        ctg.remove(existing.get().id);
        return null;
    }

}
