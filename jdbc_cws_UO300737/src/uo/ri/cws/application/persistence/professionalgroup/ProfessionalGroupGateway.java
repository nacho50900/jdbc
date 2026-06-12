package uo.ri.cws.application.persistence.professionalgroup;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.persistence.Gateway;

public interface ProfessionalGroupGateway extends
		Gateway<uo.ri.cws.application.persistence.professionalgroup.
		ProfessionalGroupGateway.ProfessionalGroupRecord> {
	
    public Optional<ProfessionalGroupRecord> findByName(String name);

	public boolean hasContracts(String groupId);
	
    public class ProfessionalGroupRecord {
    	
    	public String id;
    	public long version;
    	
    	public String name;
    	public double trienniumPayment;
    	public double productivityRate;
    	
    	public LocalDateTime createdAt;
    	public LocalDateTime updatedAt;
    	public String entityState;
    	
    	public ProfessionalGroupRecord() {
    	    this.createdAt = LocalDateTime.now();
    	    this.updatedAt = LocalDateTime.now();
    	    this.entityState = "";
    	}
    	
    	public ProfessionalGroupRecord(
    			String groupName, double trienniumSalary, double productivityRate) {
    		
    		this.name = groupName;
    		this.trienniumPayment = trienniumSalary;
    		this.productivityRate = productivityRate;
    		this.id = UUID.randomUUID().toString(); //Done here?

    	    this.createdAt = LocalDateTime.now();
    	    this.updatedAt = LocalDateTime.now();
    	    this.entityState = "";
    	}

    }

}
