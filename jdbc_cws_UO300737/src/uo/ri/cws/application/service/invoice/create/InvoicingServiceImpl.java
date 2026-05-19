package uo.ri.cws.application.service.invoice.create;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.commands.FindNotInvoiceWorkOrdersByClientNif;
import uo.ri.cws.application.service.invoice.create.commands.InvoiceWorkOrder;
import uo.ri.util.exception.BusinessException;

public class InvoicingServiceImpl implements InvoicingService {

	@Override
	public InvoiceDto create(List<String> workOrderIds)
			throws BusinessException {
		
		InvoiceWorkOrder ci = new InvoiceWorkOrder(workOrderIds);
		return ci.execute();
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByClientNif(String nif)
			throws BusinessException {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(
			String nif) throws BusinessException {
		
		FindNotInvoiceWorkOrdersByClientNif find = 
				new FindNotInvoiceWorkOrdersByClientNif(nif);
		return find.execute();
	}

	@Override
	public List<InvoicingWorkOrderDto> findWorkOrdersByPlateNumber(String plate)
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<InvoiceDto> findInvoiceByNumber(Long number) 
			throws BusinessException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<PaymentMeanDto> findPayMeansByClientNif(String nif) 
			throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void settleInvoice(String invoiceId, Map<String, Double> charges) 
			throws BusinessException {
		// TODO Auto-generated method stub
		
	}

}
