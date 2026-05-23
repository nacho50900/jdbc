package uo.ri.cws.application.service.contract.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.commands.AddContract;
import uo.ri.cws.application.service.contract.crud.commands.FindAllContracts;
import uo.ri.cws.application.service.contract.crud.commands.ListInForceContracts;
import uo.ri.cws.application.service.contract.crud.commands.ListMechanicContractsByNif;
import uo.ri.util.exception.BusinessException;

public class ContractCrudServiceImpl implements ContractCrudService {

    private CommandExecutor executor = new CommandExecutor();
    
	@Override
	public ContractDto create(ContractDto c) throws BusinessException {
		return executor.execute(new AddContract(c));
	}

	@Override
	public void update(ContractDto dto) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate(String contractId) throws BusinessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ContractDto> findById(String id) throws BusinessException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ContractSummaryDto> findByMechanicNif(String nif)
			throws BusinessException {
		return executor.execute(new ListMechanicContractsByNif(nif));
	}

	@Override
	public List<ContractDto> findInforceContracts() throws BusinessException {
		return executor.execute(new ListInForceContracts());
	}

	@Override
	public List<ContractSummaryDto> findAll() throws BusinessException {
		return executor.execute(new FindAllContracts());
	}

}
