package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
	
		// Get info
		String id = Console.readString("Type mechahic id to update");
		// check mechanic exists
		ArgumentChecks.isNotBlank(id);
	
		MechanicDto dto = new MechanicDto();
		dto.id = id;
	
		// Ask for new data
		// nif is the identity, cannot be changed
		dto.id = id;
		String name = Console.readString("Name");
		String surname = Console.readString("Surname");
		ArgumentChecks.isNotBlank(name);
		ArgumentChecks.isNotBlank(surname);
		dto.name = name;
		dto.surname = surname;
	
		MechanicCrudService mcs = Factories.service.forMechanicCrudService();
		
		try {
		    mcs.update(dto);
		    Console.println("Mechanic updated");
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}

    }

}