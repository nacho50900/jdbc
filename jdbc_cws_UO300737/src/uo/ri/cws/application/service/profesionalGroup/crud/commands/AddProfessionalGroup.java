package uo.ri.cws.application.service.profesionalGroup.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddProfessionalGroup implements Command<ProfessionalGroupDto> {

	/*When creating a new professional group, the user must provide the group name
	(name), the triennium payment (trienniumSalary), and the productivity bonus
	percentage (productivityRate). All fields are mandatory. String fields cannot be
	null or empty, and numeric fields must be greater than or equal to zero.*/
	

    private ProfessionalGroupDto dto;
    private ProfessionalGroupGateway mg = 
    		Factories.persistence.forProfessionalGroup();

    public AddProfessionalGroup(ProfessionalGroupDto dto) {
	ArgumentChecks.isNotNull(dto, "ProfessionalGroupDto cannot be null");
	ArgumentChecks.isNotBlank(dto.name, "groupName cannot be blank");
	ArgumentChecks.isTrue(dto.trienniumPayment >= 0 , 
			"trienniumSalary must be >= 0");
	ArgumentChecks.isTrue(dto.productivityRate >= 0 , 
			"productivityRate must be >= 0");
	this.dto = dto;
    }
    
	@Override
	public ProfessionalGroupDto execute() throws BusinessException {
		Optional<ProfessionalGroupRecord> op = mg.findByName(dto.name);
		BusinessChecks.doesNotExist(
				op, "The Professional Group does already exists"); // Repeated
		// Rellena los campos generados en la capa de aplicación
	    if (dto.id == null || dto.id.isBlank()) {
	        dto.id = java.util.UUID.randomUUID().toString();
	    }
	    if (dto.version == 0) {
	        dto.version = 1L;
	    }
		
		mg.add(ProfessionalGroupAssembler.toRecord(dto));
		return dto;
	}
	
	
}
