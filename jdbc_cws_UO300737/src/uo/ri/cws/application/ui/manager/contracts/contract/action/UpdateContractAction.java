package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.time.LocalDate;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractAction implements Action {

    @Override
    public void execute() throws BusinessException {

        String id = Console.readString("Contract id");

        // Find contract by id
        throw new UnsupportedOperationException("Not yet implemented");

//		Console.println("Contract updated");
    }

    private LocalDate askOptionalForDate(String msg) {
        while (true) {
            try {
                Console.print(msg + " [optional]: ");
                String asString = Console.readString();
                return ("".equals(asString)) ? null : LocalDate.parse(asString);
            } catch (Exception e) {
                Console.println("--> Invalid date");
            }
        }
    }
}