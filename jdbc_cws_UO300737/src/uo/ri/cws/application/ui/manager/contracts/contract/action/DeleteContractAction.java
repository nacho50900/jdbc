package uo.ri.cws.application.ui.manager.contracts.contract.action;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteContractAction implements Action {

    @Override
    public void execute() throws BusinessException {

        String id = Console.readString("Contract id");

        throw new UnsupportedOperationException("Not yet implemented");

//		Console.println("The contract has been deleted");
    }

}
