package uo.ri.cws.application.ui.manager.mechanic.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        // Get info
    	String selection = Console.readString("Want to search by Id or Nif");
		MechanicCrudService mcs = Factories.service.
				forMechanicCrudService();
		Optional<MechanicDto> m = null;
		
    	if (selection.toUpperCase().equals("ID")) {
    		String idMechanic = Console.readString("Type mechanic id ");
    		m = mcs.findById(idMechanic);
    	} 
    	if (selection.toUpperCase().equals("NIF")) {
    		String nifMechanic = Console.readString("Type mechanic nif ");
    		m = mcs.findByNif(nifMechanic);
    	}

    	if (m.isPresent()) {
    		Console.printf("\t%s %s %s %s %d\n", m.get().id, m.get().name, 
    				m.get().surname, m.get().nif, m.get().version);
    	} else {
    		System.out.println("Mechanic not found");
    	}

    }
}