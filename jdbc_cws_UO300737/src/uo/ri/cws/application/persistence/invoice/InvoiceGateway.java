package uo.ri.cws.application.persistence.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;

public interface InvoiceGateway extends
    Gateway<uo.ri.cws.application.persistence.invoice.
    InvoiceGateway.InvoiceRecord> {
	
    public long findNextNumber() throws PersistenceException;

    // Better via WorkOrder
	//public List<InvoicingWorkOrderRecord> findNotInvoicedWorkOrdersByClientNif(
	//		String nif) throws PersistenceException;
    //public Optional<InvoiceRecord> findByNif(String nif) throws PersistenceException;
    
    public static class InvoiceRecord {
		public String id;
		public long version;

		public double amount;
		public double vat;	
		public long number;	
		public LocalDate date;
		public String state;
		
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;
		
		public InvoiceRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
    }
    
	public static class InvoicingWorkOrderRecord {
		public String id;
		public String description;
		public LocalDateTime date;
		public String state;
		public double amount;
		
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;
		
		public InvoicingWorkOrderRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
	}

}
