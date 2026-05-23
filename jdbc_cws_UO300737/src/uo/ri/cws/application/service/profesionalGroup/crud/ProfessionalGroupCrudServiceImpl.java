package uo.ri.cws.application.service.profesionalGroup.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.AddProfessionalGroup;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.DeleteProfessionalGroup;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.ListAllProfessionalGroups;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.ListProfessionalGroupByName;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.UpdateProfessionalGroup;
import uo.ri.util.exception.BusinessException;

public class ProfessionalGroupCrudServiceImpl
        implements ProfessionalGroupCrudService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public ProfessionalGroupDto create(ProfessionalGroupDto dto)
            throws BusinessException {
        return executor.execute(new AddProfessionalGroup(dto));
    }

    @Override
    public void delete(String name) throws BusinessException {
        executor.execute(new DeleteProfessionalGroup(name));
    }

    @Override
    public void update(ProfessionalGroupDto dto) throws BusinessException {
        executor.execute(new UpdateProfessionalGroup(dto));
    }

    @Override
    public Optional<ProfessionalGroupDto> findByName(String name)
            throws BusinessException {
        return executor.execute(new ListProfessionalGroupByName(name));
    }

    @Override
    public List<ProfessionalGroupDto> findAll() throws BusinessException {
        return executor.execute(new ListAllProfessionalGroups());
    }

}