package uo.ri.cws.application.service.profesionalGroup.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class ListProfessionalGroupById 
	implements Command<ProfessionalGroupDto> {

	private String id;
    private ProfessionalGroupGateway mp = 
    		Factories.persistence.forProfessionalGroup();
    
    public ListProfessionalGroupById(String id) {
    	ArgumentChecks.isNotNull(id, "id cannot be null");
    	ArgumentChecks.isNotBlank(id, "id cannot be blank");
    	this.id = id;
    }

	@Override
	public ProfessionalGroupDto execute() throws BusinessException {
		Optional<ProfessionalGroupRecord> op = mp.findById(id);
		//No need of BussinesChecks.doesNotExist();
		if (op.isEmpty()) {
			//throw new BusinessException("Professional Group thoes not exist");
			//Just return null
			return null;
		}
		return ProfessionalGroupAssembler.toDto(op.get());
	}

}
