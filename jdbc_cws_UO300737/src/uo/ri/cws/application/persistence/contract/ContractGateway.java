package uo.ri.cws.application.persistence.contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.util.exception.BusinessException;

public interface ContractGateway extends 
Gateway<uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord> {


	public static class ContractRecord {
	    public String id;                     // surrogate id (UUID)
	    public long version;

	    public String mechanicId;            // ID del mecánico asociado
	    public String contractTypeId;        // ID del tipo de contrato
	    public String professionalGroupId;   // ID del grupo profesional

	    public LocalDate startDate;          // Fecha de inicio del contrato
	    public LocalDate endDate;            // Fecha de fin del contrato
	    public double annualBaseSalary;      // Salario base anual
	    public double taxRate;               // Tasa impositiva

	    public double settlement;            // Liquidación calculada
	    public String state;                 // Estado del contrato
	}
	
	public static class ContractSummaryRecord {
	    public String id;                     // surrogate id (UUID)
	    public long version;

	    public String nif;
	    public int numPayrolls;				 // Filled in reading operations
	    public double settlement;            // Liquidación calculada
	    public String state; 				 // Estado del contrato

	}

	public Optional<List<ContractSummaryRecord>> findByMechanicNif(String nif) 
			throws BusinessException;
	
	public Optional<List<ContractRecord>> findInForceContracts() 
			throws BusinessException;
	
}
