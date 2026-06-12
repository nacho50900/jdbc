package uo.ri.cws.application.persistence.workorder;

import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkorderRecord;

public interface WorkOrderGateway extends Gateway<WorkorderRecord> {

    public List<WorkorderRecord> findNotInvoicedWorkOrdersByNif(String nif);

    public List<WorkorderRecord> findByMechanicId(String id);

    public class WorkorderRecord {
	public String id;
	public long version;

	public String description;
	public LocalDateTime date;
	public String state;
	public double amount;
	public String invoiceId;
	public String mechanicId;
	public String vehicleId;

	public LocalDateTime createdAt;
	public LocalDateTime updatedAt;
	public String entityState;

	public WorkorderRecord() {
	    this.createdAt = LocalDateTime.now();
	    this.updatedAt = LocalDateTime.now();
	    this.entityState = "";
	}
    }

}
