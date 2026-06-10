package uo.ri.cws.application.service.profesionalgroup.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddProfessionalGroup implements Command<ProfessionalGroupDto> {

	/**
	 * When creating a new professional group, the user must provide the group name
	 * (name), the triennium payment (trienniumSalary), and the productivity bonus 
	 * percentage (productivityRate). All fields are mandatory. String fields cannot be
	 * null or empty, and numeric fields must be greater than or equal to zero.
	*/

	private ProfessionalGroupDto dto;
    private ProfessionalGroupGateway pg =
            Factories.persistence.forProfessionalGroup();
 
    public AddProfessionalGroup(ProfessionalGroupDto dto) {
        ArgumentChecks.isNotNull(dto, "ProfessionalGroupDto cannot be null");
        ArgumentChecks.isNotBlank(dto.name, "name cannot be blank");
        ArgumentChecks.isTrue(dto.trienniumPayment >= 0,
                "trienniumPayment must be >= 0");
        ArgumentChecks.isTrue(dto.productivityRate >= 0,
                "productivityRate must be >= 0");
        this.dto = dto;
    }
 
    @Override
    public ProfessionalGroupDto execute() throws BusinessException {
        Optional<ProfessionalGroupRecord> op = pg.findByName(dto.name);
        BusinessChecks.doesNotExist(op,
                "A professional group with that name already exists");
 
        if (dto.id == null || dto.id.isBlank()) {
            dto.id = java.util.UUID.randomUUID().toString();
        }
        if (dto.version == 0) {
            dto.version = 1L;
        }
 
        pg.add(ProfessionalGroupAssembler.toRecord(dto));
        return dto;
    }
}
