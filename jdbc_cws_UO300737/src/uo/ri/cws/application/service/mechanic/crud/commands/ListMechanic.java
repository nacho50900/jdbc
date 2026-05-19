package uo.ri.cws.application.service.mechanic.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicAssembler;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public abstract class ListMechanic implements Command<MechanicDto> {//

    private String IdOrNif;
    private String selectedSearch;
    private MechanicGateway mg = Factories.persistence.forMechanic();

    public ListMechanic(String selectedSearch, String IdOrNif) {
	this.selectedSearch = selectedSearch;
	ArgumentChecks.isNotNull(IdOrNif, "Mechanic id or nif cannot be null");
	ArgumentChecks.isNotBlank(IdOrNif,
	    "Mechanic id or nif cannot be blank");
	this.IdOrNif = IdOrNif;
    }

    @Override
	public MechanicDto execute() throws BusinessException {
	Optional<MechanicRecord> om;
	if (selectedSearch.equals("ID")) {
		System.out.println("HERE: " + IdOrNif);
	    om = mg.findById(IdOrNif);
	} else {
	    om = mg.findByNif(IdOrNif);
	}
	//BusinessChecks.(om, "The mechanic does not exist");
	//Segun tests no se lanza exception, solo se devuelve optional de null
	if (om.isEmpty()) {
		return null; 
	}
	return MechanicAssembler.toDto(om.get());
    }
}
