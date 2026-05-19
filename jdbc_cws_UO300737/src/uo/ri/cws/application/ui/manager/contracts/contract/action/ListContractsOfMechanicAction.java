package uo.ri.cws.application.ui.manager.contracts.contract.action;

import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListContractsOfMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {

        String nif = Console.readString("Mechanic nif");

        throw new UnsupportedOperationException("Not yet implemented");

        /*
         * List<ContractSummaryDto> contracts = as.findByMechanicNif( nif );
         * 
         * for(ContractSummaryDto c: contracts) { Printer.printContractSummary(
         * c ); }
         */
    }
}
