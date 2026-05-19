package uo.ri.cws.application.service.profesionalGroup.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.AddProfessionalGroup;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.DeleteProfessionalGroup;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.ListAllProfessionalGroups;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.ListProfessionalGroupByName;
import uo.ri.cws.application.service.profesionalGroup.crud.commands.UpdateProfessionalGroup;
import uo.ri.util.exception.BusinessException;

public class ProfessionalGroupCrudServiceImpl 
	implements ProfessionalGroupCrudService {

	@Override
	public ProfessionalGroupDto create(ProfessionalGroupDto dto) 
			throws BusinessException {
		
		AddProfessionalGroup ap = new AddProfessionalGroup(dto);
		return ap.execute();
	}

	@Override
	public void delete(String name) throws BusinessException {
		DeleteProfessionalGroup dp = new DeleteProfessionalGroup(name);
		dp.execute();
	}

	@Override
	public void update(ProfessionalGroupDto dto) throws BusinessException {
		UpdateProfessionalGroup up = new UpdateProfessionalGroup(dto);
		up.execute();
	}

	@Override
	public Optional<ProfessionalGroupDto> findByName(String name) 
			throws BusinessException {
		ListProfessionalGroupByName lp = new ListProfessionalGroupByName(name);
		ProfessionalGroupDto dto = lp.execute();
		if(dto == null) {
			return Optional.empty();
		}
		return Optional.of(dto);
	}
	
	//public Optional<ProfessionalGroupDto> findByName(String name)
	// NOT IN THE PROVIDED INTERFACE

	@Override
	public List<ProfessionalGroupDto> findAll() throws BusinessException {
		ListAllProfessionalGroups lp = new ListAllProfessionalGroups();
		return lp.execute();
	}

}
