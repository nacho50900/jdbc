package uo.ri.cws.application.persistence;

import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.impl.ContractGatewayImpl;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.cws.application.persistence.contracttype.impl.ContractTypeGatewayImpl;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.cws.application.persistence.intervention.impl.InterventionGatewayImpl;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.persistence.invoice.impl.InvoiceGatewayImpl;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.impl.MechanicGatewayImpl;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.impl.PayrollGatewayImpl;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.impl.ProfessionalGroupGatewayImpl;
import uo.ri.cws.application.persistence.sparePart.SparePartGateway;
import uo.ri.cws.application.persistence.sparePart.impl.SparePartGatewayImpl;
import uo.ri.cws.application.persistence.substitution.SubstitutionGateway;
import uo.ri.cws.application.persistence.substitution.impl.SubstitutionGatewayImpl;
import uo.ri.cws.application.persistence.supply.SupplyGateway;
import uo.ri.cws.application.persistence.supply.impl.SupplyGatewayImpl;
import uo.ri.cws.application.persistence.vehicle.VehicleGateway;
import uo.ri.cws.application.persistence.vehicle.impl.VehicleGatewayImpl;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.cws.application.persistence.workorder.impl.WorkOrderGatewayImpl;

public class PersistenceFactory {

    public MechanicGateway forMechanic() {
        return new MechanicGatewayImpl();
    }

    public WorkOrderGateway forWorkOrder() {
        return new WorkOrderGatewayImpl();
    }

    public InvoiceGateway forInvoice() {
        return new InvoiceGatewayImpl();
    }

    public InterventionGateway forIntervention() {
        return new InterventionGatewayImpl();
    }

    public SparePartGateway forSparePart() {
        return new SparePartGatewayImpl();
    }

    public SupplyGateway forSupplyGateway() {
        return new SupplyGatewayImpl();
    }

    public SubstitutionGateway forSubstitutionsGateway() {
        return new SubstitutionGatewayImpl();
    }

    public VehicleGateway forVehicle() {
        return new VehicleGatewayImpl();
    }

    public ContractGateway forContract() {
        return new ContractGatewayImpl();
    }

    public ProfessionalGroupGateway forProfessionalGroup() {
        return new ProfessionalGroupGatewayImpl();

    }

    public ContractTypeGateway forContractType() {
        return new ContractTypeGatewayImpl();
    }

    public PayrollGateway forPayroll() {
        return new PayrollGatewayImpl();
    }

}
