package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.mechanic.MechanicAssembler;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class UpdateMechanic implements Command<Void> {

    private MechanicDto dto;
    private MechanicGateway mg = Factories.persistence.forMechanic();

    public UpdateMechanic(MechanicDto dto) {
	ArgumentChecks.isNotNull(dto);
	ArgumentChecks.isNotBlank(dto.id);
	ArgumentChecks.isNotBlank(dto.nif);
	ArgumentChecks.isNotBlank(dto.name);
	ArgumentChecks.isNotBlank(dto.surname);
	this.dto = dto;
    }

    @Override
    public Void execute() throws BusinessException {

	Optional<MechanicRecord> om = mg.findById(dto.id);
	BusinessChecks.exists(om, "The mecahnic does not exist");
	BusinessChecks.hasVersion(dto.version, om.get().version);

	try {
		mg.update(MechanicAssembler.toRecord(dto));
	} catch (PersistenceException pe) {
		throw new BusinessException(pe.getMessage());
	} //Should launch exception
	BusinessChecks.hasVersion(dto.version, om.get().version);

	return null;
    }

}
