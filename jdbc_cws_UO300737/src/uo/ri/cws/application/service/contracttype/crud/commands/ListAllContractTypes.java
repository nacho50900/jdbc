package uo.ri.cws.application.service.contracttype.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contracttype.ContractTypeAssembler;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.exception.BusinessException;

public class ListAllContractTypes implements Command<List<ContractTypeDto>> {

    private final ContractTypeGateway ctg =
            Factories.persistence.forContractType();

    @Override
    public List<ContractTypeDto> execute() throws BusinessException {
        return ContractTypeAssembler.toDtoList(ctg.findAll());
    }

}
