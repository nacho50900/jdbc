package uo.ri.cws.application.service.invoice.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.invoice.InvoiceAssembler;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;
import uo.ri.util.math.Rounds;

public class InvoiceWorkOrder implements Command<InvoiceDto> {

    private List<String> workOrderIds = new ArrayList<String>();
    private InvoiceDto dto;
    private InvoiceGateway ig = Factories.persistence.forInvoice();

    public InvoiceWorkOrder(List<String> WorkOrderIds)  {
		ArgumentChecks.isNotNull(WorkOrderIds, "workOrderIds can not be null");
		ArgumentChecks.isTrue(!WorkOrderIds.isEmpty(), "workOrderIds can not be empty");
		for (String id : WorkOrderIds) {
			ArgumentChecks.isNotNull(id, "workOrderId can not be blank");
		}

		this.workOrderIds = WorkOrderIds;
		dto = new InvoiceDto();
    }//Record should be an exact copy of the table.

    @Override
	public InvoiceDto execute() throws BusinessException {

        try {
        	if (!checkWorkOrdersExist(workOrderIds)) {
    				throw new BusinessException("Workorder does not exist");
    		    }
    		    if (!checkWorkOrdersFinished(workOrderIds)) {
    		    	throw new BusinessException("Workorder is not finished yet");
    		    }
	        long numberInvoice = generateInvoiceNumber(dto.id);
		    LocalDate dateInvoice = LocalDate.now();
		    double amount = calculateTotalInvoice(workOrderIds); // vat not
									 // included
		    double vat = (amount == 0.0) ? 0.0 : vatPercentage(dateInvoice); //Fixed error
		    double vatAmount = amount * (vat / 100); // vat amount
		    double total = amount * vatAmount; // vat included (SHOULD BE + ?)
		    total = Rounds.toCents(total);
		    String idInvoice = UUID.randomUUID()
			       .toString();

	 		// Create and execute
	 		dto.id = idInvoice;
	 		dto.number = numberInvoice;
	 		dto.date = dateInvoice;
	 		dto.vat = vat;
	 		dto.amount = total;
			dto.version = 1L; 
			dto.state = "NOT_YET_PAID"; 

	        if (dto.id != null && !dto.id.isBlank()) {
	            Optional<InvoiceRecord> om = ig.findById(dto.id);
	            BusinessChecks.doesNotExist(om, "The invoice already exists");}
	        
			ig.add(InvoiceAssembler.toRecord(dto));	
		    
		    linkWorkordersToInvoice(idInvoice, workOrderIds);
		    markWorkOrderAsInvoiced(workOrderIds);
		    updateVersion(workOrderIds);
		    updateTimeVersion(workOrderIds);
	 		
        } catch (SQLException e) {
        	throw new RuntimeException(e.getMessage());
        }
        
		return dto;
    }

    /*
     * checks whether every work order exist
     */
    private boolean checkWorkOrdersExist(List<String> ids) throws SQLException {
	    if (ids == null || ids.isEmpty()) {
			return false;
		}
	
	    Connection c = Jdbc.getCurrentConnection();
	    if (c == null || c.isClosed()) {
	    	c = Jdbc.createThreadConnection(); //Need it first use
	    }
	    String placeholders = String.join(",", 
	    		java.util.Collections.nCopies(ids.size(), "?"));
	    String sql = "SELECT COUNT(*) FROM TWORKORDERS WHERE id IN (" +
	    		placeholders + ")";
	
	    try (PreparedStatement pst = c.prepareStatement(sql)) {
	        int i = 1;
	        for (String id : ids) {
				pst.setString(i++, id);
			}
	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                long count = rs.getLong(1);
	                return count == ids.size(); // deben existir TODAS
	            }
	        }
	    }
	    return false;
}

    
    /*
     * checks whether every work order id is FINISHED
     */

    private boolean checkWorkOrdersFinished(List<String> workOrderIDs) 
    		throws SQLException {
    	
	    Connection c = Jdbc.getCurrentConnection();
	    try (PreparedStatement pst = c.prepareStatement(
	            Queries.getSQLSentence("TWORKORDERS_FIND_STATUS_BY_ID"))) {
	        for (String id : workOrderIDs) {
	            pst.setString(1, id);
	            try (ResultSet rs = pst.executeQuery()) {
	                if (!rs.next()) {
	                    // Si no existe ese workorder, NO está "todo finished"
	                    return false;
	                }
	                String status = rs.getString("state");
	                if (!"FINISHED".equalsIgnoreCase(status)) {
	                    return false;
	                }
	            }
	        }
	    }
	    return true;
}


    /*
     * Generates next invoice number (not to be confused with the inner id)
     */
    private long generateInvoiceNumber(String id) throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
	
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TINVOICES_FIND_NUMBER_BY_ID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
				    return rs.getLong(1) + 1;
				}
			}
		}
		return 1L; // Si no hay facturas previas, empezamos desde 1
    }

    /*
     * Compute total amount of the invoice (as the total of individual work
     * orders' amount
     */
    private double calculateTotalInvoice(List<String> workOrderIDs)
	throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
		
		double total = 0.0;
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_FIND_AMOUNT_BY_ID"))) {
		    for (String id : workOrderIDs) {
		    	pst.setString(1, id);
				try (ResultSet rs = pst.executeQuery()) {
				    if (rs.next()) {
				    	total += rs.getDouble("amount");
				    }
				}
		    }
		}
		return total;
    }

    /*
     * returns vat percentage
     */
    private double vatPercentage(LocalDate d) {
	return LocalDate.parse("2012-07-01")
			.isBefore(d) ? 21.0 : 18.0;

    }
    

	private void updateTimeVersion(List<String> workOrderIds)
	throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
	
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_UPDATE_TIMESTAMP"))) {
		    for (String workOrderID : workOrderIds) {
				pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
				pst.setString(2, workOrderID);
				pst.executeUpdate();
		    }
		}
    }

    private void updateVersion(List<String> workOrderIds) throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
	
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_UPDATE_VERSION"))) {
		    for (String workOrderID : workOrderIds) {
			pst.setString(1, workOrderID);
			pst.executeUpdate();
		    }
		}
    }


    /*
     * Set the invoice number field in work order table to the invoice number
     * generated
     */
    private void linkWorkordersToInvoice(String invoiceId,
	List<String> workOrderIDs) throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
	
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_UPDATE_INVOICEID"))) {
		    for (String id : workOrderIDs) {
			pst.setString(1, invoiceId);
			pst.setString(2, id);
			pst.executeUpdate();
		    }
		}
    }

    /*
     * Sets status to INVOICED for every workorder
     */
    private void markWorkOrderAsInvoiced(List<String> ids) throws SQLException {
		Connection c = Jdbc.getCurrentConnection();
	
		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_UPDATE_STATE"))) {
		    for (String id : ids) {
			pst.setString(1, id);
			pst.executeUpdate();
		    }
		}
    }

    void displayInvoice(long numberInvoice, LocalDate dateInvoice,
	double totalInvoice, double vat, double totalConIva) {
		Console.printf("Invoice number: %d\n", numberInvoice);
		Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", dateInvoice);
		Console.printf("\tAmount: %.2f €\n", totalInvoice);
		Console.printf("\tVAT: %.1f %% \n", vat);
		Console.printf("\tTotal (including VAT): %.2f €\n", totalConIva);
    }
}
