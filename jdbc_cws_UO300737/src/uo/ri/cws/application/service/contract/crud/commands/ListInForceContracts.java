package uo.ri.cws.application.service.contract.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractAssembler;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.exception.BusinessException;

public class ListInForceContracts  implements Command<List<ContractDto>>{

    private ContractGateway cg = Factories.persistence.forContract();

    @Override
	public List<ContractDto> execute() throws BusinessException {
		Optional<List<ContractRecord>> om = cg.findInForceContracts();
		
		if (om.isEmpty()) {
			return new ArrayList<ContractDto>();
		}
		return ContractAssembler.toDtoList(om.get());
    }
}