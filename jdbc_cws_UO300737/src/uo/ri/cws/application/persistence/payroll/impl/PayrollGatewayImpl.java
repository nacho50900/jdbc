package uo.ri.cws.application.persistence.payroll.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.payroll.PayrollAssembler;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class PayrollGatewayImpl implements PayrollGateway {

	private static final int SCALE = 3;
	private static final int SCALE_NET_SALARY = 2;
	private static final RoundingMode RM = RoundingMode.HALF_UP;
	
	@Override
	public void add(PayrollRecord t) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String id) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PayrollRecord t) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int deleteLastGenerated() throws PersistenceException {

		//It is more robust to calculate the time intervals here than in the sql
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1);
		LocalDate lastDayOfPreviousMonth = now.withDayOfMonth(1).minusDays(1);
		
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_DELETE_LAST_GENERATED"))) {

			pst.setDate(1, java.sql.Date.valueOf(firstDayOfPreviousMonth));
			pst.setDate(2, java.sql.Date.valueOf(lastDayOfPreviousMonth));

            return pst.executeUpdate();
	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	}
	
	@Override
	public int deleteLastGeneratedByMechanicId(String mechanicId)
			throws PersistenceException {
		
		LocalDate now = LocalDate.now();
		LocalDate firstDayOfPreviousMonth = now.minusMonths(1).withDayOfMonth(1);
		LocalDate lastDayOfPreviousMonth = now.withDayOfMonth(1).minusDays(1);
		
	    Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                		"TPAYROLLS_DELETE_LAST_GENERATED_BY_MECHANIC_ID"))) {
        	
        	pst.setString(1, mechanicId);
			pst.setDate(2, java.sql.Date.valueOf(firstDayOfPreviousMonth));
			pst.setDate(3, java.sql.Date.valueOf(lastDayOfPreviousMonth));
			
            return pst.executeUpdate();
	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	}
	

	@Override
	public Optional<PayrollRecord> findById(String id)
			throws PersistenceException {
		
	    PayrollDto dto = new PayrollDto();
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_BY_ID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    dto.id = rs.getString("ID");
                    dto.contractId = rs.getString("CONTRACT_ID");
                    dto.date = rs.getDate("DATE").toLocalDate();
                    dto.baseSalary = rs.getDouble("BASESALARY");
                    dto.extraSalary = rs.getDouble("EXTRASALARY");
                    dto.productivityEarning = rs.getDouble(
                    		"PRODUCTIVITYEARNING");
                    dto.trienniumEarning = rs.getDouble("TRIENNIUMEARNING");
                    dto.taxDeduction = rs.getDouble("TAXDEDUCTION");
                    dto.nicDeduction = rs.getDouble("NICDEDUCTION");
                    
                    dto.grossSalary = dto.baseSalary + dto.extraSalary + 
                    		dto.productivityEarning + dto.trienniumEarning;
                    dto.totalDeductions = dto.taxDeduction + dto.nicDeduction;
                    dto.netSalary = dto.grossSalary - dto.totalDeductions;
                    
                    dto.version = rs.getLong("VERSION");
                } else {
                    return Optional.empty();
                }
            } 
	    }catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	    return Optional.of(PayrollAssembler.toRecord(dto));
	}
	
	@Override
	public List<PayrollRecord> findAll() throws PersistenceException {
		
	    List<PayrollRecord> payrollList = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_ALL"))) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PayrollDto dto = new PayrollDto();
                    dto.id = rs.getString("ID");
                    dto.contractId = rs.getString("CONTRACT_ID");
                    dto.date = rs.getDate("DATE").toLocalDate();
                    dto.baseSalary = rs.getDouble("BASESALARY");
                    dto.extraSalary = rs.getDouble("EXTRASALARY");
                    dto.productivityEarning = rs.getDouble(
                    		"PRODUCTIVITYEARNING");
                    dto.trienniumEarning = rs.getDouble("TRIENNIUMEARNING");
                    dto.taxDeduction = rs.getDouble("TAXDEDUCTION");
                    dto.nicDeduction = rs.getDouble("NICDEDUCTION");
                    
                    dto.grossSalary = dto.baseSalary + dto.extraSalary + 
                    		dto.productivityEarning + dto.trienniumEarning;
                    dto.totalDeductions = dto.taxDeduction + dto.nicDeduction;
                    dto.netSalary = dto.grossSalary - dto.totalDeductions;
                    
                    dto.version = rs.getLong("VERSION");

                    payrollList.add(PayrollAssembler.toRecord(dto));
                }
            }
        } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	    return payrollList;
	}
	
	@Override
	public List<PayrollSummaryRecord> findpayrollsByMechanicId(
			String mechanicId) throws PersistenceException {
		
	    List<PayrollSummaryRecord> summaries = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();
	    
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_BY_MECHANIC_ID"))) {
            pst.setString(1, mechanicId);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PayrollSummaryDto dto = new PayrollSummaryDto();
                    dto.id = rs.getString("ID");
                    dto.date = rs.getDate("DATE").toLocalDate();
                    Double baseSalary = rs.getDouble("basesalary");
                    Double extraSalary = rs.getDouble("extrasalary");
                    Double productivityEarning = rs.getDouble(
                    		"productivityearning");
                    Double trienniumEarning = rs.getDouble("trienniumearning");
                    Double taxDeduction = rs.getDouble("taxdeduction");
                    Double nicDeduction = rs.getDouble("nicdeduction");

                    Double grossSalary = baseSalary + extraSalary + 
                    		productivityEarning + trienniumEarning;
                    Double totalDeductions = taxDeduction + nicDeduction;
                    dto.netSalary = grossSalary - totalDeductions;
                    summaries.add(PayrollAssembler.toSummaryRecord(dto));
                }
            }
	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	    return summaries;
	}
	
	@Override
	public List<PayrollSummaryRecord> findPayrollsByProfessionalGroupName(
			String groupName) throws PersistenceException {
	    
		List<PayrollSummaryRecord> summaries = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                		"TPAYROLLS_FIND_SUMMARY_BY_PROF_GROUP_NAME"))) {
            pst.setString(1, groupName);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PayrollSummaryDto dto = new PayrollSummaryDto();
                    dto.id = rs.getString("ID");
                    dto.date = rs.getDate("DATE").toLocalDate();
                    Double baseSalary = rs.getDouble("basesalary");
                    Double extraSalary = rs.getDouble("extrasalary");
                    Double productivityEarning = rs.getDouble(
                    		"productivityearning");
                    Double trienniumEarning = rs.getDouble("trienniumearning");
                    Double taxDeduction = rs.getDouble("taxdeduction");
                    Double nicDeduction = rs.getDouble("nicdeduction");

                    Double grossSalary = baseSalary + extraSalary + 
                    		productivityEarning + trienniumEarning;
                    Double totalDeductions = taxDeduction + nicDeduction;
                    dto.netSalary = grossSalary - totalDeductions;

                    summaries.add(PayrollAssembler.toSummaryRecord(dto));
                }
            }
	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }

	    return summaries;
	}


	@Override
	public List<PayrollRecord> generateForPrevMonth() {
		
	    List<PayrollRecord> payrolls = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();
	    
	    LocalDate today = LocalDate.now();
	    LocalDate start = today.minusMonths(1).withDayOfMonth(1);
	    LocalDate end = today.withDayOfMonth(1);

        if (alreadyGeneratedForPrevMonthof(today)) {
            try (PreparedStatement pst = c.prepareStatement(
                    Queries.getSQLSentence(
                    		"TPAYROLLS_GENERATE_FOR_PREVIOUS_MONTH"))) {

                pst.setDate(1, java.sql.Date.valueOf(start));
                pst.setDate(2, java.sql.Date.valueOf(end));

                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        PayrollDto dto = new PayrollDto();
                        dto.id = rs.getString("id");
                        dto.version = rs.getLong("version");
                        dto.contractId = rs.getString("contract_id");
                        dto.date = rs.getDate("date").toLocalDate();

                        dto.baseSalary = rs.getDouble("basesalary");
                        dto.extraSalary = rs.getDouble("extrasalary");
                        dto.productivityEarning = rs.getDouble(
                        		"productivityearning");
                        dto.trienniumEarning = rs.getDouble("trienniumearning");
                        dto.taxDeduction = rs.getDouble("taxdeduction");
                        dto.nicDeduction = rs.getDouble("nicdeduction");

                        dto.grossSalary = dto.baseSalary + dto.extraSalary + 
                        		dto.productivityEarning + dto.trienniumEarning;
                        dto.totalDeductions = dto.taxDeduction + 
                        		dto.nicDeduction;
                        dto.netSalary = dto.grossSalary - dto.totalDeductions;

                        payrolls.add(PayrollAssembler.toRecord(dto));
                    }
                }
             
            } catch (SQLException e) {
    	        throw new RuntimeException("Error generating or retrieving "
    	        		+ "payrolls for previous month", e);
    	    }
        } else {
            // Generar nuevas nóminas
            List<ContractDto> contracts = getActiveContractsBetween(start, end);

            for (ContractDto contract : contracts) {
                PayrollDto dto = calculatePayroll(contract, start);
                insertPayroll(dto);
                payrolls.add(PayrollAssembler.toRecord(dto));
            }
        }
	    return payrolls;
	}
		
	@Override
	public List<PayrollRecord> generateForPrevMonthof(LocalDate present) {
		
	    List<PayrollRecord> payrolls = new ArrayList<>();
	    
	    LocalDate start = present.minusMonths(1).withDayOfMonth(1);
	    LocalDate end = present.withDayOfMonth(1);

		// 1. Obtener contratos activos en el mes anterior
		List<ContractDto> contracts = getActiveContractsBetween(start, end);
		
		for (ContractDto contract : contracts) {
			
		    // 2. Calcular valores de nómina
		    PayrollDto dto = calculatePayroll(contract, start);

		    // 3. Insertar nómina en la base de datos
		    insertPayroll(dto);

		    payrolls.add(PayrollAssembler.toRecord(dto));
		}
	    return payrolls;
	}

	@Override
	public boolean alreadyGeneratedForPrevMonthof(LocalDate present) {
		
	    LocalDate start = present.minusMonths(1).withDayOfMonth(1);
	    LocalDate end = present.withDayOfMonth(1);
    	Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
        		Queries.getSQLSentence("TPAYROLLS_GENERATED_FOR_MOTH"))) {

		    pst.setDate(1, java.sql.Date.valueOf(start));
		    pst.setDate(2, java.sql.Date.valueOf(end));
		
		    try (ResultSet rs = pst.executeQuery()) {
		        if (rs.next()) {
		            return rs.getInt(1) > 0;
		        }
		    }
	    } catch (SQLException e) {
		    throw new RuntimeException("Error checking if payrolls already "
		    		+ "exist for previous month of " + present, e);
		}
	    return false;
	}
	
	private List<ContractDto> getActiveContractsBetween(
			LocalDate start, LocalDate end) {
		
	    List<ContractDto> contracts = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
	    		Queries.getSQLSentence("T_CONTRACTS_GET_ACTIVES_BY_MONTH"))) {

	    	pst.setDate(1, java.sql.Date.valueOf(end)); // end excluido

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                ContractDto dto = new ContractDto();
	                dto.id = rs.getString("ID");
	                dto.annualBaseSalary = rs.getDouble("ANNUALBASESALARY");
	                dto.startDate = rs.getDate("STARTDATE").toLocalDate();
	                if (rs.getDate("ENDDATE") != null) {
	                	dto.endDate = rs.getDate("ENDDATE").toLocalDate();
	                } else {
	                	dto.endDate = null;
	                }
	                dto.taxRate = rs.getDouble("TAXRATE");
	                contracts.add(dto);
	            }
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error retrieving active contracts", e);
	    }
	    return contracts;
	}

	private PayrollDto calculatePayroll(
			ContractDto contract, LocalDate payrollMonthStart) {
		
	    PayrollDto dto = new PayrollDto();
	    dto.contractId = contract.id;

	    //Fecha nómina = último día del mes de nómina (mes anterior a present)
	    dto.date = payrollMonthStart.withDayOfMonth(
	    		payrollMonthStart.lengthOfMonth());

	    BigDecimal annual = BigDecimal.valueOf(contract.annualBaseSalary);
	    BigDecimal taxRate = BigDecimal.valueOf(contract.taxRate);
	    BigDecimal prodRate = BigDecimal.valueOf(
	    		getProductivityRateForContract(contract.id));
	    BigDecimal trienUnit = BigDecimal.valueOf(
	    		getTrienniumPayForContract(contract.id));
	    // FIX: usar payrollMonthStart (sin minusMonths(1))
	    BigDecimal invoiced = BigDecimal.valueOf(
	    		getInvoicedAmountForContract(contract.id, payrollMonthStart));

	    BigDecimal base14 = annual.divide(BigDecimal.valueOf(14), 10, RM);
	    BigDecimal base = base14.setScale(SCALE, RM);

	    //Extra payment in summer and christmas vacations
	    BigDecimal extra = BigDecimal.ZERO;
	    if (dto.date.getMonthValue() == 6 || dto.date.getMonthValue() == 12) {
	    	extra = base;
	    }
	    long years = ChronoUnit.YEARS.between(contract.startDate, dto.date);
	    long triennia  = years / 3;
	    BigDecimal triennium = trienUnit.multiply(BigDecimal.valueOf(triennia))
	                                      .setScale(SCALE, RM);
	    BigDecimal productivity= prodRate.multiply(invoiced).
	    		setScale(SCALE, RM);
	    BigDecimal gross = base.add(extra).add(productivity).add(triennium);
	    BigDecimal tax = taxRate.multiply(gross).setScale(SCALE, RM);

	    // It is calculated BY MOTNH - FIX: nic discrenpancy
	    BigDecimal monthly12 = annual.divide(BigDecimal.valueOf(12), 10, RM);
	    BigDecimal nic = monthly12.multiply(BigDecimal.valueOf(0.05))
	                                      .setScale(SCALE, RM);
	    BigDecimal totalDed = tax.add(nic).setScale(SCALE, RM);

	    // Net salary jsut up to a 2 decimales
	    BigDecimal net = gross.subtract(totalDed).
	    		setScale(SCALE_NET_SALARY, RM);

	    // Asignar al DTO
	    dto.baseSalary = base.doubleValue();
	    dto.extraSalary = extra.doubleValue();
	    dto.productivityEarning = productivity.doubleValue();
	    dto.trienniumEarning = triennium.doubleValue();
	    dto.taxDeduction = tax.doubleValue();
	    dto.nicDeduction  = nic.doubleValue();
	    dto.grossSalary = gross.doubleValue();       
	    dto.totalDeductions = totalDed.doubleValue();    
	    dto.netSalary = net.doubleValue();
       
	    return dto;
	}
	
	private double getTrienniumPayForContract(String contractId) {
		
		Connection c = Jdbc.getCurrentConnection();
		
	    try (PreparedStatement pst = c.prepareStatement(
	    		Queries.getSQLSentence(
	    				"TPAYROLLS_GET_TRIENNIUMPAYMENT_BY_ID"))) {

	        pst.setString(1, contractId);

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                return rs.getDouble("trienniumPayment");
	            }
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error retrieving triennium pay "
	        		+ "for contract " + contractId, e);
	    }

	    return 0.0;
	}
	
	private double getProductivityRateForContract(String contractId) {

		Connection c = Jdbc.getCurrentConnection();

		try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence("TPAYROLLS_GET_PRODUCTIVITY_RATE_BY_ID"))) {

	        pst.setString(1, contractId);

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                return rs.getDouble("productivityrate");
	            }
	        }
		
		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving productivity "
					+ "rate for contract " + contractId, e);
		}

	    return 0.0;
	}

	private double getInvoicedAmountForContract(
			String contractId, LocalDate monthStart) {
		
	    LocalDate start = monthStart.withDayOfMonth(1);
	    LocalDate endExclusive = start.plusMonths(1);
	    
	    Connection c = Jdbc.getCurrentConnection();

	    // 1) Principal: Sum per mechanic of contract
	    double byMechanic = 0.0;
	    try (PreparedStatement pst = c.prepareStatement(
	            Queries.getSQLSentence("TPAYROLLS_GET_INVOICEDAMOUNT_BY_ID"))) {

	        pst.setString(1, contractId);
	        pst.setTimestamp(2, Timestamp.valueOf(start.atStartOfDay()));
	        pst.setTimestamp(3, Timestamp.valueOf(endExclusive.atStartOfDay()));

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
					byMechanic = rs.getDouble("total");
				}
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error retrieving invoiced amount "
	        		+ "(by mechanic) for contract " + contractId, e);
	    }

	    if (byMechanic > 0.0) {
	        return byMechanic;
	    }

	    //2) FIX: Fallback: Sum all the workOrders of the month
	    // (null mech or others)
	    double byMonth = 0.0;
	    try (PreparedStatement pst = c.prepareStatement(
	            Queries.getSQLSentence("TPAYROLLS_GET_INVOICEDAMOUNT_MONTH"))) {

	        pst.setTimestamp(1, Timestamp.valueOf(start.atStartOfDay()));
	        pst.setTimestamp(2, Timestamp.valueOf(endExclusive.atStartOfDay()));

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
					byMonth = rs.getDouble("total");
				}
	        }
	    } catch (SQLException e) {
	        throw new RuntimeException("Error retrieving invoiced amount "
	        		+ "(by month)", e);
	    }
	    return byMonth;
	}
	
	private void insertPayroll(PayrollDto dto) {
		
		Connection c = Jdbc.getCurrentConnection();
	
    	try(PreparedStatement pst = c.prepareStatement(
    			Queries.getSQLSentence("TPAYROLLS_INSERT"))) {

	        pst.setString(1, UUID.randomUUID().toString());
	        pst.setLong(2, 1L);
	        pst.setString(3, dto.contractId);
	        pst.setDate(4, java.sql.Date.valueOf(dto.date));
	        //Changed in order to save 2 and 3 decimals of precision
	        pst.setBigDecimal(5, BigDecimal.valueOf(
	        		dto.baseSalary).setScale(SCALE, RM));
	        pst.setBigDecimal(6, BigDecimal.valueOf(
	        		dto.extraSalary).setScale(SCALE, RM));
	        pst.setBigDecimal(7, BigDecimal.valueOf(
	        		dto.productivityEarning).setScale(SCALE, RM));
	        pst.setBigDecimal(8, BigDecimal.valueOf(
	        		dto.trienniumEarning).setScale(SCALE, RM));
	        pst.setBigDecimal(9, BigDecimal.valueOf(
	        		dto.taxDeduction).setScale(SCALE, RM));
	        pst.setBigDecimal(10,BigDecimal.valueOf(
	        		dto.nicDeduction).setScale(SCALE, RM));
	        pst.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
	        pst.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
	        pst.setString(13, "ACTIVE");

			pst.executeUpdate();
    	
    	} catch (SQLException e) {
    		throw new RuntimeException("Error inserting payroll", e);
    	}
	   
    }
}