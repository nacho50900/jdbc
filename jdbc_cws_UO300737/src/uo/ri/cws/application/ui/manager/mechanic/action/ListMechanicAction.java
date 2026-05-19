package uo.ri.cws.application.ui.manager.mechanic.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListMechanicAction implements Action {


    @Override
    public void execute() throws BusinessException {

        // Get info
    	String selection = Console.readString("Want to search by Id or Nif");
    	
    	if (selection.toUpperCase().equals("ID")) {
    		String idMechanic = Console.readString("Type mechanic id ");
    		MechanicCrudService mcs = Factories.service.
    				forMechanicCrudService();
    		mcs.findById(idMechanic);
    	} 
    	if (selection.toUpperCase().equals("NIF")) {
    		String nifMechanic = Console.readString("Type mechanic nif ");
    		MechanicCrudService mcs = Factories.service.
    				forMechanicCrudService();
    		mcs.findByNif(nifMechanic);
    	}
    }
}