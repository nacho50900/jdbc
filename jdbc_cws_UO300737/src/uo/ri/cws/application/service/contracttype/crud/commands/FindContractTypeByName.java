package uo.ri.cws.application.service.contracttype.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contracttype.ContractTypeAssembler;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindContractTypeByName
        implements Command<Optional<ContractTypeDto>> {

    private final String name;
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    public FindContractTypeByName(String name) {
        ArgumentChecks.isNotNull(name, "Name cannot be null");
        ArgumentChecks.isNotBlank(name, "Name cannot be blank");
        this.name = name;
    }

    @Override
    public Optional<ContractTypeDto> execute() throws BusinessException {
        Optional<ContractTypeRecord> record = ctg.findByName(name);
        if (record.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ContractTypeAssembler.toDto(record.get()));
    }

}
