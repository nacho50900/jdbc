package uo.ri.cws.application.persistence.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.exception.BusinessException;

public interface InvoiceGateway extends
    Gateway<uo.ri.cws.application.persistence.invoice.
    InvoiceGateway.InvoiceRecord> {

    public static class InvoiceRecord {
		public String id;		// the surrogate id (UUID)
		public long version;

		public double amount;	// total amount (money) vat included
		public double vat;		// amount of vat (money)
		public long number;		// the invoice identity, a sequential number
		public LocalDate date;	// of the invoice
		public String state;	// the state as in InvoiceState
    }

	public Optional<InvoiceRecord> findByNif(String nif);

	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(
			String nif) throws BusinessException;
}
