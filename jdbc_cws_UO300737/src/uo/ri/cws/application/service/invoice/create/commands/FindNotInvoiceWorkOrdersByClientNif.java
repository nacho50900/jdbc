package uo.ri.cws.application.service.invoice.create.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoiceWorkOrdersByClientNif {
 
    private String nif;
    private InvoiceGateway ig = Factories.persistence.forInvoice();
    
    public FindNotInvoiceWorkOrdersByClientNif(String nif) {
    	ArgumentChecks.isNotNull(nif, "NIF cannot be null");
    	//ArgumentChecks.isNotBlank(nif, "NIF cannot be blank");
    	//Test demand an empty list
    	this.nif = nif;
    }
    
    public List<InvoicingWorkOrderDto> execute() throws BusinessException {
    	Console.println("\nClient's not invoiced work orders\n");
    	
		//Optional<InvoiceRecord> om = ig.findByNif(nif);
		//BusinessChecks.exists(om, "The invoice does not exist"); 
		//Test say it just return an empty list if necesary, we can
		//have a invoice with no workorders 
		return ig.findNotInvoicedWorkOrdersByClientNif(nif);        
    }
    
}
