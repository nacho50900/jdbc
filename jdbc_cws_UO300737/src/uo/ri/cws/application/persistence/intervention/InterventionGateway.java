package uo.ri.cws.application.persistence.intervention;

import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.intervention.InterventionGateway.InterventionRecord;

public interface InterventionGateway extends Gateway<InterventionRecord> {

    public List<InterventionRecord> findByMechanicId(String id)
	throws PersistenceException;

    public class InterventionRecord {
	public String id;
	public long version;

	public int minutes;
	public LocalDateTime date;
	public String mechanicId;
	public String workOrderId;

	public LocalDateTime createdAt;
	public LocalDateTime updatedAt;
	public String entityState;

	public InterventionRecord() {
	    this.createdAt = LocalDateTime.now();
	    this.updatedAt = LocalDateTime.now();
	    this.entityState = "";
		}
    }

}
