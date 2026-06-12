package uo.ri.cws.application.persistence.payroll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;

public interface PayrollGateway extends Gateway<PayrollRecord>{

	boolean alreadyGeneratedForPrevMonthof(LocalDate present);

	public List<PayrollSummaryRecord> findpayrollsByMechanicId(
			String mechanicId) throws PersistenceException;

	public List<PayrollSummaryRecord> findPayrollsByProfessionalGroupName(
			String groupName) throws PersistenceException;

	public int deleteLastGenerated() throws PersistenceException;

	public int deleteLastGeneratedByMechanicId(String mechanicId)
			throws PersistenceException;

	public List<PayrollRecord> findByContractId(String id);
	
    public class PayrollRecord {

    	public String id;
    	public long version;
    	
    	public String contractId;
    	public LocalDate date;
    	
    	// Earnings
    	public double baseSalary;
    	public double extraSalary;
    	public double productivityEarning;
    	public double trienniumEarning;
    	
    	// Deductions
    	public double taxDeduction;
    	public double nicDeduction;
    	
    	// Net wage
    	public double netSalary;
		public double grossSalary;
		public double totalDeductions;
		
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;
		
		public PayrollRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
    }
    
    public class PayrollSummaryRecord {

    	public String id;
    	
    	public LocalDate date;
    	public double netSalary;
    	
		public LocalDateTime createdAt;
		public LocalDateTime updatedAt;
		public String entityState;
		
		public PayrollSummaryRecord() {
		    this.createdAt = LocalDateTime.now();
		    this.updatedAt = LocalDateTime.now();
		    this.entityState = "";
	    }
    }

}
