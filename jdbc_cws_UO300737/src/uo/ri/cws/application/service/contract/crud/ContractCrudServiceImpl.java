package uo.ri.cws.application.service.contract.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.commands.AddContract;
import uo.ri.cws.application.service.contract.crud.commands.DeleteContract;
import uo.ri.cws.application.service.contract.crud.commands.FindAllContracts;
import uo.ri.cws.application.service.contract.crud.commands.FindContractById;
import uo.ri.cws.application.service.contract.crud.commands.FindInForceContracts;
import uo.ri.cws.application.service.contract.crud.commands.FindContractsByMechanicNif;
import uo.ri.cws.application.service.contract.crud.commands.TerminateContract;
import uo.ri.cws.application.service.contract.crud.commands.UpdateContract;
import uo.ri.util.exception.BusinessException;

public class ContractCrudServiceImpl implements ContractCrudService {

    private CommandExecutor executor = new CommandExecutor();
    
	@Override
	public ContractDto create(ContractDto c) throws BusinessException {
		return executor.execute(new AddContract(c));
	}

	@Override
    public void update(ContractDto dto) throws BusinessException {
        executor.execute(new UpdateContract(dto));
    }

    @Override
    public void delete(String id) throws BusinessException {
        executor.execute(new DeleteContract(id));
    }

    @Override
    public void terminate(String contractId) throws BusinessException {
        executor.execute(new TerminateContract(contractId));
    }

    @Override
    public Optional<ContractDto> findById(String id)
            throws BusinessException {
        return executor.execute(new FindContractById(id));
    }

	@Override
	public List<ContractSummaryDto> findByMechanicNif(String nif)
			throws BusinessException {
		return executor.execute(new FindContractsByMechanicNif(nif));
	}

	@Override
	public List<ContractDto> findInforceContracts() throws BusinessException {
		return executor.execute(new FindInForceContracts());
	}

	@Override
	public List<ContractSummaryDto> findAll() throws BusinessException {
		return executor.execute(new FindAllContracts());
	}

}
