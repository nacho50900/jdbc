package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicAssembler;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddMechanic
    implements Command<MechanicDto> {

    private MechanicDto dto;
    private MechanicGateway mg = Factories.persistence.forMechanic();

    public AddMechanic(MechanicDto dto) {
	ArgumentChecks.isNotNull(dto, "Mechanic cannot be null");
	ArgumentChecks.isNotBlank(dto.nif, "NIF cannot be blank");
	ArgumentChecks.isNotBlank(dto.name, "Name cannot be blank");
	ArgumentChecks.isNotBlank(dto.surname, "Surname cannot be blank");
	this.dto = dto;
    }

    @Override
    public MechanicDto execute() throws BusinessException {
	Optional<MechanicRecord> om = mg.findByNif(dto.nif);
	BusinessChecks.doesNotExist(om, "The mechanic already exists"); 

	// Rellena los campos generados en la capa de aplicación
    if (dto.id == null || dto.id.isBlank()) {
        dto.id = java.util.UUID.randomUUID().toString();
    }
    if (dto.version == 0) {
        dto.version = 1L;
    }

	mg.add(MechanicAssembler.toRecord(dto));
	return dto;
    }
}
