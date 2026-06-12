package uo.ri.cws.application.persistence.contract;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;

public interface ContractGateway extends 
Gateway<uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord> {

	public List<ContractSummaryRecord> findByMechanicNif(String nif) 
			throws PersistenceException;
	
	public List<ContractRecord> findInForceContracts() 
			throws PersistenceException;

	public Optional<ContractRecord> findInForceByMechanicId(String id);

	public boolean mechanicHasWorkOrders(String mechanicId);

	public boolean hasPayrolls(String id);
	
	public static class ContractRecord {
	    public String id;            
	    public long version;

	    public String mechanicId;        
	    public String contractTypeId;    
	    public String professionalGroupId;   

	    public LocalDate startDate;   
	    public LocalDate endDate;        
	    public double annualBaseSalary;    
	    public double taxRate;           

	    public double settlement;  
	    public String state;             
	    
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;

		public ContractRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
	}

	
	public static class ContractSummaryRecord {
	    public String id;                  
	    public long version;

	    public String nif;
	    public int numPayrolls;			
	    public double settlement;         
	    public String state; 			

	    
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;
		
		public ContractSummaryRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
	}
	
}
