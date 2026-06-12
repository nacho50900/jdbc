package uo.ri.cws.application.persistence.contracttype;

import java.time.LocalDateTime;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ContractTypeGateway extends Gateway<uo.ri.cws.
application.persistence.contracttype.ContractTypeGateway.ContractTypeRecord> {

    Optional<ContractTypeRecord> findByName(String name);

    int countContractsFor(String contractTypeId);

    public class ContractTypeRecord {

	public String id;
	public long version;

	public String name;
	public double compensationDays;

	public LocalDateTime createdAt;
	public LocalDateTime updatedAt;
	public String entityState;

	public ContractTypeRecord() {
	    this.createdAt = LocalDateTime.now();
	    this.updatedAt = LocalDateTime.now();
	    this.entityState = "";
	}
    }
    	    
}
