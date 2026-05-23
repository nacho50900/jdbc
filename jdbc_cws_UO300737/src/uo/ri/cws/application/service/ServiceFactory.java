package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.crud.ContractCrudServiceImpl;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.crud.ContractTypeCrudServiceImpl;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.create.InvoicingServiceImpl;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.mechanic.crud.MechanicCrudServiceImpl;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.PayrollServiceImpl;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalGroup.crud.ProfessionalGroupCrudServiceImpl;
import uo.ri.cws.application.service.spare.SparePartCrudService;
import uo.ri.cws.application.service.vehicle.VehicleCrudService;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService;
import uo.ri.cws.application.service.workorder.CloseWorkOrderService;
import uo.ri.cws.application.service.workorder.ViewAssignedWorkOrdersService;
import uo.ri.cws.application.service.workorder.WorkOrderCrudService;
import uo.ri.util.exception.NotYetImplementedException;

public class ServiceFactory {

    // the not yet implemented section ------------------------------

    public MechanicCrudService forMechanicCrudService() {
    	return new MechanicCrudServiceImpl();
    }

    public InvoicingService forCreateInvoiceService() {
    	return new InvoicingServiceImpl();
    }

    public VehicleCrudService forVehicleCrudService() {
	throw new NotYetImplementedException();
    }

    public SparePartCrudService forSparePartCrudService() {
	throw new NotYetImplementedException();
    }

    public ContractCrudService forContractCrudService() {
    	return new ContractCrudServiceImpl();
    }

    public ContractTypeCrudService forContractTypeCrudService() {
    	return new ContractTypeCrudServiceImpl();
    }

    public PayrollService forPayrollService() {
    	return new PayrollServiceImpl();
    }

    public ProfessionalGroupCrudService forProfessionalGroupCrudService() {
    	return new ProfessionalGroupCrudServiceImpl();
    }

    public ClientCrudService forClientCrudService() {
	throw new NotYetImplementedException();
    }

    public CloseWorkOrderService forClosingWorkOrder() {
	throw new NotYetImplementedException();
    }

    public VehicleTypeCrudService forVehicleTypeCrudService() {
	throw new NotYetImplementedException();
    }

    public ClientHistoryService forClientHistoryService() {
	throw new NotYetImplementedException();
    }

    public WorkOrderCrudService forWorkOrderService() {
	throw new NotYetImplementedException();
    }

    public ViewAssignedWorkOrdersService forViewAssignedWorkOrdersService() {
	throw new NotYetImplementedException();
    }

}
