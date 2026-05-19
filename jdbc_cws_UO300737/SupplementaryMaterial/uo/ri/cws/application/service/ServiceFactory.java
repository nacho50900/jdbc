package uo.ri.cws.application.service;

import uo.ri.cws.application.service.client.ClientCrudService;
import uo.ri.cws.application.service.client.ClientHistoryService;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.mechanic.MechanicCrudService;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.professionalgroup.ProfessionalGroupCrudService;
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
        throw new NotYetImplementedException();
    }

    public InvoicingService forCreateInvoiceService() {
        throw new NotYetImplementedException();
    }

    public VehicleCrudService forVehicleCrudService() {
        throw new NotYetImplementedException();
    }

    public SparePartCrudService forSparePartCrudService() {
        throw new NotYetImplementedException();
    }

    public ContractCrudService forContractCrudService() {
        throw new NotYetImplementedException();
    }

    public ContractTypeCrudService forContractTypeCrudService() {
        throw new NotYetImplementedException();
    }

    public PayrollService forPayrollService() {
        throw new NotYetImplementedException();
    }

    public ProfessionalGroupCrudService forProfessionalGroupCrudService() {
        throw new NotYetImplementedException();
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
