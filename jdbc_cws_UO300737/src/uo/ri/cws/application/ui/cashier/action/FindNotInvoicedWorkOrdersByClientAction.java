package uo.ri.cws.application.ui.cashier.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindNotInvoicedWorkOrdersByClientAction implements Action {

    /**
     * Process: 
     * - Ask customer nif 
     * - Display all uncharged workorder (status <> 'INVOICED'). 
     *   For each workorder, display id, vehicle id, date, status, amount 
     *   and description
     */

    @Override
    public void execute() throws BusinessException {
    	//Get Info
        String nif = Console.readString("Client nif ");

        InvoicingService ics = Factories.service.forCreateInvoiceService();
		ics.findNotInvoicedWorkOrdersByClientNif(nif);
    }

}