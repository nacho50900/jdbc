package uo.ri.cws.application.service.contracttype.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.crud.commands.AddContractType;
import uo.ri.cws.application.service.contracttype.crud.commands.DeleteContractType;
import uo.ri.cws.application.service.contracttype.crud.commands.FindContractTypeByName;
import uo.ri.cws.application.service.contracttype.crud.commands.FindAllContractTypes;
import uo.ri.cws.application.service.contracttype.crud.commands.UpdateContractType;
import uo.ri.util.exception.BusinessException;

public class ContractTypeCrudServiceImpl implements ContractTypeCrudService {

    private final CommandExecutor executor = new CommandExecutor();

    @Override
    public ContractTypeDto create(ContractTypeDto dto)
            throws BusinessException {
        return executor.execute(new AddContractType(dto));
    }

    @Override
    public void update(ContractTypeDto dto) throws BusinessException {
        executor.execute(new UpdateContractType(dto));
    }

    @Override
    public void delete(String name) throws BusinessException {
        executor.execute(new DeleteContractType(name));
    }

    @Override
    public Optional<ContractTypeDto> findByName(String name)
            throws BusinessException {
        return executor.execute(new FindContractTypeByName(name));
    }

    @Override
    public List<ContractTypeDto> findAll() throws BusinessException {
        return executor.execute(new FindAllContractTypes());
    }

}
