package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.time.LocalDate;
import java.time.LocalDateTime;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddContractAction implements Action {

    /**
     * Creates a contract. The mechanic's NIF, contract type name, type name,
     * month and base salary are provided via console.
     */
    @Override
    public void execute() throws BusinessException {

        String nif = Console.readString("Mechanic NIF");
        String contractTypeName = askForType();
        String professionalGroupName = askForPorfessionalGroup();
        double annualBaseSalary = Console.readDouble("Annual base salary");
        LocalDate endDate;
        if ("FIXED_TERM".equals(contractTypeName)) {
            endDate = Console.readDate("Type end date");
        }

        /*
         * Add the contract
         */
        throw new UnsupportedOperationException("Not yet implemented");

//		Console.println("Contract registered");
    }

    private String askForPorfessionalGroup() {
        Console.println("Professional group");
        Console.println("I \t II \t III \t IV \t V \t VI \t VII");
        return Console.readString("Professional group name");
    }

    private String askForType() {
        Console.println("Contract type");
        Console.println("PERMANENT \t SEASONAL \t FIXED_TERM");
        return Console.readString("Contract type name");
    }

}