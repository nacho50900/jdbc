package uo.ri.cws.application.ui.manager.contracts.contracttype.action;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractTypeAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String name = Console.readString("Contract type name");

        // Check contract type exists
        throw new UnsupportedOperationException("Not yet implemented");

//		double compensationDays = Console.readDouble("Compensation days");
//
//		// update 
//		
//		Console.println("Contract type updated");
    }

}