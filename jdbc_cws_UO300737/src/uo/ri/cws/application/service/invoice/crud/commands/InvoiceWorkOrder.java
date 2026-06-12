package uo.ri.cws.application.service.invoice.crud.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkorderRecord;
import uo.ri.cws.application.service.invoice.InvoiceAssembler;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.math.Rounds;

public class InvoiceWorkOrder implements Command<InvoiceDto> {

    private List<String> workOrderIds = new ArrayList<String>();
    private InvoiceGateway ig = Factories.persistence.forInvoice();
    private WorkOrderGateway wg = Factories.persistence.forWorkOrder();

    public InvoiceWorkOrder(List<String> WorkOrderIds)  {
		ArgumentChecks.isNotNull(WorkOrderIds, "workOrderIds can not be null");
		ArgumentChecks.isTrue(!WorkOrderIds.isEmpty(), 
				"workOrderIds can not be empty");
		for (String id : WorkOrderIds) {
			ArgumentChecks.isNotNull(id, "workOrderId can not be blank");
			ArgumentChecks.isNotEmpty(id, "workOrderId can not be empty");
		}

		this.workOrderIds = WorkOrderIds;
    }

    @Override
	public InvoiceDto execute() throws BusinessException {
	
		List<WorkorderRecord> workorders = getWorkOrders();
	
		for (WorkorderRecord wo : workorders) {
		    if (wo.state != "FINISHED") {
			throw new BusinessException("WorkOrder isn't finished yet");
		    }
		}
	
		long numberInvoice = ig.findNextNumber();
		double amount = calculateTotalInvoice(workorders);
	
		InvoiceRecord invoice = createInvoice(numberInvoice, amount);
	
		updateWorkOrders(workorders, invoice.id);
		
		return InvoiceAssembler.toDto(invoice);
    }
    
    /*
     * Compute total amount of the invoice
     */
    private double calculateTotalInvoice(List<WorkorderRecord> workOrders) {
		double totalInvoice = 0;
		for (WorkorderRecord wo : workOrders) {
		    totalInvoice += wo.amount;
		}
		return totalInvoice;
    }

    private InvoiceRecord createInvoice(long numberInvoice, double amount) {
		InvoiceRecord invoice = new InvoiceRecord();
		invoice.id = UUID.randomUUID().toString();
		invoice.version = 1;
		invoice.createdAt = LocalDateTime.now();
		invoice.updatedAt = LocalDateTime.now();
		invoice.entityState = "";
		invoice.date = LocalDate.now();
		invoice.vat = (amount * (vatPercentage(invoice.date) / 100));
		invoice.amount = Rounds.toCents(amount + invoice.vat);
		invoice.number = numberInvoice;
		invoice.state = "NOT_YET_PAID";
		ig.add(invoice);
	
		return invoice;
    }


    private double vatPercentage(LocalDate d) {
	return LocalDate.parse("2012-07-01")
			.isBefore(d) ? 21.0 : 18.0;
    }
    
    private List<WorkorderRecord> getWorkOrders() throws BusinessException {
    	List<WorkorderRecord> workorders = new ArrayList<WorkorderRecord>();
    	for (String id : workOrderIds) {
    	    Optional<WorkorderRecord> wo = wg.findById(id);
    	    if (!wo.isEmpty()) {
    	    	workorders.add(wo.get());
    	    } else {
    	    	throw new BusinessException("Workorder does not exist");
    	    }
    	}
    	return workorders;
    }
    
    private void updateWorkOrders(List<WorkorderRecord> workorders,
	String invoiceId) {
		for (WorkorderRecord wr : workorders) {
		    wr.invoiceId = invoiceId;
		    wr.state = "INVOICED";
		    wg.update(wr);
		}
    }
}
