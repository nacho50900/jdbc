package uo.ri.cws.application.service.contract.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractAssembler;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractById implements Command<Optional<ContractDto>> {

    private final String id;
    private final ContractGateway cg = Factories.persistence.forContract();

    public FindContractById(String id) {
        ArgumentChecks.isNotNull(id, "Contract id cannot be null");
        ArgumentChecks.isNotBlank(id, "Contract id cannot be blank");
        this.id = id;
    }

    @Override
    public Optional<ContractDto> execute() throws BusinessException {
        Optional<ContractRecord> record = cg.findById(id);
        if (record.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ContractAssembler.toDto(record.get()));
    }

}
