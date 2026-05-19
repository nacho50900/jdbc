package uo.ri.cws.application.ui.cashier.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class InvoiceWorkorderAction implements Action {
    
    @Override
    public void execute() throws BusinessException {
        List<String> workOrderIds = new ArrayList<>();

        // Get Info - Ask the user the work order ids
        do {
            String id = Console.readString("Workorder id");
            workOrderIds.add(id);
        } while (moreWorkOrders());
        
		InvoicingService ics = Factories.service.forCreateInvoiceService();
		ics.create(workOrderIds);
    }


    private boolean moreWorkOrders() {
        return Console.readString("more work orders? (y/n) ")
                .equalsIgnoreCase("y");
    }

}
