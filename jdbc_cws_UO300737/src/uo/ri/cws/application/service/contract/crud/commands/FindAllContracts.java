package uo.ri.cws.application.service.contract.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractAssembler;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

public class FindAllContracts implements Command<List<ContractSummaryDto>> {

    private ContractGateway cg = Factories.persistence.forContract();

    @Override
    public List<ContractSummaryDto> execute() {
    	List<ContractDto> contracts = ContractAssembler.toDtoList(cg.findAll());
    	return ContractAssembler.toSummaryDtoList(contracts);
    	
    }
}
