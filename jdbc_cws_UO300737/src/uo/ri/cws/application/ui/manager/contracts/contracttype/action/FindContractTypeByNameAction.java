package uo.ri.cws.application.ui.manager.contracts.contracttype.action;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindContractTypeByNameAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String name = Console.readString("Contract type name");

        throw new UnsupportedOperationException("Not yet implemented");

//		Printer.printContractType(dto);
    }

}