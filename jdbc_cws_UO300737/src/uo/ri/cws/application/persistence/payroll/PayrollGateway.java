package uo.ri.cws.application.persistence.payroll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;

public interface PayrollGateway extends Gateway<PayrollRecord>{
	
    public class PayrollRecord {

    	public String id;
    	public long version;
    	
    	//Record atributes
    	public LocalDateTime createdAt;
    	public LocalDateTime updatedAt;
    	
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
    }
    
    public class PayrollSummaryRecord {

    	public String id;
    	
    	//Record atributes
    	public LocalDateTime createdAt;
    	public LocalDateTime updatedAt;
    	
    	public LocalDate date;
    	public double netSalary;
    }

	public List<PayrollRecord> generateForPrevMonth();

	public List<PayrollRecord> generateForPrevMonthof(LocalDate present);

	boolean alreadyGeneratedForPrevMonthof(LocalDate present);

	public List<PayrollSummaryRecord> findpayrollsByMechanicId(
			String mechanicId) throws PersistenceException;

	public List<PayrollSummaryRecord> findPayrollsByProfessionalGroupName(
			String groupName) throws PersistenceException;

	public int deleteLastGenerated() throws PersistenceException;

	public int deleteLastGeneratedByMechanicId(String mechanicId)
			throws PersistenceException;


}
