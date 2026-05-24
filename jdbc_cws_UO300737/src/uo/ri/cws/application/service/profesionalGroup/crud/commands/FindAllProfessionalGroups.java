package uo.ri.cws.application.service.profesionalGroup.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;

public class FindAllProfessionalGroups 
	implements Command<List<ProfessionalGroupDto>> {

    private ProfessionalGroupGateway pg = 
    		Factories.persistence.forProfessionalGroup();

    @Override
    public List<ProfessionalGroupDto> execute() {
    	return ProfessionalGroupAssembler.toDtoList(pg.findAll());
    }
}
