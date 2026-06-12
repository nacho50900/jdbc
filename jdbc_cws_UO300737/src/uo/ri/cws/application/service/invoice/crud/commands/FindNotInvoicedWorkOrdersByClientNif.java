package uo.ri.cws.application.service.invoice.crud.commands;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.service.invoice.InvoiceAssembler;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class FindNotInvoicedWorkOrdersByClientNif implements Command<List<InvoicingWorkOrderDto>>{
 
    private String nif;
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();
    
    public FindNotInvoicedWorkOrdersByClientNif(String nif) {
    	ArgumentChecks.isNotNull(nif, "NIF cannot be null");
    	//ArgumentChecks.isNotBlank(nif, "NIF cannot be blank");
    	//Test demand an empty list
    	this.nif = nif;
    }
    
    @Override
	public List<InvoicingWorkOrderDto> execute() throws BusinessException {
		//Optional<InvoiceRecord> om = ig.findByNif(nif);
		//BusinessChecks.exists(om, "The invoice does not exist"); 
		//Test say it just return an empty list if necesary, we can
		//have a invoice with no workorders 
		return InvoiceAssembler.toInvoicingDtoList(
				wg.findNotInvoicedWorkOrdersByNif(nif));   

    }
    
}
