package uo.ri.cws.application.service.contracttype.crud.commands;

import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contracttype.ContractTypeAssembler;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.ContractTypeRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContractType implements Command<ContractTypeDto> {

    private final ContractTypeDto dto;
    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    public AddContractType(ContractTypeDto dto) {
        ArgumentChecks.isNotNull(dto, "ContractTypeDto cannot be null");
        ArgumentChecks.isNotBlank(dto.name, "Name cannot be blank");
        ArgumentChecks.isTrue(dto.compensationDays >= 0,
                "Compensation days must be >= 0");
        this.dto = dto;
    }

    @Override
    public ContractTypeDto execute() throws BusinessException {
        Optional<ContractTypeRecord> existing = ctg.findByName(dto.name);
        BusinessChecks.doesNotExist(existing,
                "A contract type with that name already exists");

        dto.id = UUID.randomUUID().toString();
        dto.version = 1L;

        ctg.add(ContractTypeAssembler.toRecord(dto));
        return dto;
    }

}
