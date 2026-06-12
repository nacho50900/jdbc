package uo.ri.cws.application.service.profesionalgroup.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateProfessionalGroup implements Command<ProfessionalGroupDto>{

	private ProfessionalGroupDto dto;
	private ProfessionalGroupGateway pg = 
			Factories.persistence.forProfessionalGroup();

    public UpdateProfessionalGroup(ProfessionalGroupDto dto) {
		ArgumentChecks.isNotNull(dto);
		ArgumentChecks.isNotBlank(dto.id);
		ArgumentChecks.isNotBlank(dto.name);
		ArgumentChecks.isTrue(dto.trienniumPayment >= 0 , 
				"trienniumSalary must be >= 0");
		ArgumentChecks.isTrue(dto.productivityRate >= 0 , 
				"productivityRate must be >= 0");
		this.dto = dto;
    }
    
	@Override
	public ProfessionalGroupDto execute() throws BusinessException {
		Optional<ProfessionalGroupRecord> om = pg.findById(dto.id); 
		BusinessChecks.exists(om, "The ProfessionalGroup does not exist");
		BusinessChecks.hasVersion(dto.version, om.get().version);
		
		try {
			pg.update(ProfessionalGroupAssembler.toRecord(dto));
		} catch (PersistenceException pe) {
			throw new BusinessException(pe.getMessage());
		} //Should launch exception
		BusinessChecks.hasVersion(dto.version, om.get().version);

		return null;
	}
}
