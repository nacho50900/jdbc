package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {

		// Get info
		MechanicDto dto = new MechanicDto();
		dto.nif = Console.readString("nif");
		dto.name = Console.readString("Name");
		dto.surname = Console.readString("Surname");
	
		/*
		 * AddMechanic am = new AddMechanic(dto); am.execute();
		 */
		// Generates coupling, is better to use a service interface
		// By the way, the service interface should NOT be modified
	
		MechanicCrudService mcs = Factories.service.forMechanicCrudService();
		dto = mcs.create(dto);
	
		try {
		    mcs.create(dto);
		    Console.println("Mechanic added" + dto.id);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}

    }

}
