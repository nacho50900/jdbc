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
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class PayrollGatewayImpl implements PayrollGateway {

	private static final int SCALE = 3;
	private static final int SCALE_NET_SALARY = 2;
	private static final RoundingMode RM = RoundingMode.HALF_UP;
	
    // Private POJO - only used inside this gateway, no service dependency
    private static class ContractData {
        String id;
        double annualBaseSalary;
        double taxRate;
        LocalDate startDate;
        @SuppressWarnings("unused")
		LocalDate endDate;
        // I do not know why eclipse mark it as unsued it can be clearly see how
        // it is used in lines 240 and 242
    }
    
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
        LocalDate firstDay = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDay  = now.withDayOfMonth(1).minusDays(1);
 
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_DELETE_LAST_GENERATED"))) {
 
            pst.setDate(1, java.sql.Date.valueOf(firstDay));
            pst.setDate(2, java.sql.Date.valueOf(lastDay));
            return pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
	
    @Override
    public int deleteLastGeneratedByMechanicId(String mechanicId)
            throws PersistenceException {
 
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDay  = now.withDayOfMonth(1).minusDays(1);
 
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                        "TPAYROLLS_DELETE_LAST_GENERATED_BY_MECHANIC_ID"))) {
 
            pst.setString(1, mechanicId);
            pst.setDate(2, java.sql.Date.valueOf(firstDay));
            pst.setDate(3, java.sql.Date.valueOf(lastDay));
            return pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
	

    @Override
    public Optional<PayrollRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_BY_ID"))) {
 
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(toRecord(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
	
    @Override
    public List<PayrollRecord> findAll() throws PersistenceException {
        List<PayrollRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_ALL"));
             ResultSet rs = pst.executeQuery()) {
 
            while (rs.next()) {
                result.add(toRecord(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }
	
    @Override
    public List<PayrollSummaryRecord> findpayrollsByMechanicId(String mechanicId)
            throws PersistenceException {
 
        List<PayrollSummaryRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_FIND_BY_MECHANIC_ID"))) {
 
            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(toSummaryRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }
	
    @Override
    public List<PayrollSummaryRecord> findPayrollsByProfessionalGroupName(
            String groupName) throws PersistenceException {
 
        List<PayrollSummaryRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                        "TPAYROLLS_FIND_SUMMARY_BY_PROF_GROUP_NAME"))) {
 
            pst.setString(1, groupName);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(toSummaryRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }


    @Override
    public boolean alreadyGeneratedForPrevMonthof(LocalDate present) {
        LocalDate start = present.minusMonths(1).withDayOfMonth(1);
        LocalDate end   = present.withDayOfMonth(1);
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
            throw new PersistenceException(e);
        }
        return false;
    }
 
    @Override
    public List<PayrollRecord> generateForPrevMonth() {
        return generateForPrevMonthof(LocalDate.now());
    }
		
    @Override
    public List<PayrollRecord> generateForPrevMonthof(LocalDate present) {
        List<PayrollRecord> payrolls = new ArrayList<>();
        LocalDate start = present.minusMonths(1).withDayOfMonth(1);
        LocalDate end   = present.withDayOfMonth(1);
 
        List<ContractData> contracts = getActiveContractsBetween(start, end);
        for (ContractData contract : contracts) {
            PayrollRecord r = calculateAndInsertPayroll(contract, start);
            payrolls.add(r);
        }
        return payrolls;
    }
	
	private List<ContractData> getActiveContractsBetween(
			LocalDate start, LocalDate end) {
		
	    List<ContractData> contracts = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
	    		Queries.getSQLSentence("T_CONTRACTS_GET_ACTIVES_BY_MONTH"))) {

	    	pst.setDate(1, java.sql.Date.valueOf(end)); // end excluido

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                ContractData dto = new ContractData();
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
	        throw new PersistenceException("Error retrieving active contracts", e);
	    }
	    return contracts;
	}
	
    private double getTrienniumPayForContract(String contractId) {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPAYROLLS_GET_TRIENNIUMPAYMENT_BY_ID"))) {
 
            pst.setString(1, contractId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("trienniumPayment");
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
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
            throw new PersistenceException(e);
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
	        throw new PersistenceException("Error retrieving invoiced amount "
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
	        throw new PersistenceException("Error retrieving invoiced amount "
	        		+ "(by month)", e);
	    }
	    return byMonth;
	}
	
	public List<PayrollRecord> findByContractId(String contractId)
	        throws PersistenceException {

	    List<PayrollRecord> result = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
	            Queries.getSQLSentence("TPAYROLLS_FIND_BY_CONTRACT_ID"))) {

	        pst.setString(1, contractId);

	        try (ResultSet rs = pst.executeQuery()) {
	            while (rs.next()) {
	                PayrollRecord r = new PayrollRecord();
	                r.id = rs.getString("ID");
	                r.version = rs.getLong("VERSION");
	                r.contractId = rs.getString("CONTRACT_ID");
	                r.date = rs.getDate("DATE").toLocalDate();
	                r.baseSalary = rs.getDouble("BASESALARY");
	                r.extraSalary = rs.getDouble("EXTRASALARY");
	                r.productivityEarning = rs.getDouble("PRODUCTIVITYEARNING");
	                r.trienniumEarning = rs.getDouble("TRIENNIUMEARNING");
	                r.taxDeduction = rs.getDouble("TAXDEDUCTION");
	                r.nicDeduction = rs.getDouble("NICDEDUCTION");

	                r.grossSalary = r.baseSalary + r.extraSalary
	                        + r.productivityEarning + r.trienniumEarning;
	                r.totalDeductions = r.taxDeduction + r.nicDeduction;
	                r.netSalary = r.grossSalary - r.totalDeductions;

	                result.add(r);
	            }
	        }

	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }

	    return result;
	}
	
	private PayrollRecord calculateAndInsertPayroll(
            ContractData contract, LocalDate payrollMonthStart) {
 
		//Fecha nómina = último día del mes de nómina (mes anterior a present)
        LocalDate payDate = payrollMonthStart.withDayOfMonth(
                payrollMonthStart.lengthOfMonth());
 
        BigDecimal annual   = BigDecimal.valueOf(contract.annualBaseSalary);
        BigDecimal taxRate  = BigDecimal.valueOf(contract.taxRate);
        BigDecimal prodRate = BigDecimal.valueOf(
                getProductivityRateForContract(contract.id));
        BigDecimal trienUnit = BigDecimal.valueOf(
                getTrienniumPayForContract(contract.id));
        
        // FIX: usar payrollMonthStart (sin minusMonths(1))
        BigDecimal invoiced  = BigDecimal.valueOf(
                getInvoicedAmountForContract(contract.id, payrollMonthStart));
 
        BigDecimal base14 = annual.divide(BigDecimal.valueOf(14), 10, RM);
        BigDecimal base   = base14.setScale(SCALE, RM);
 
        //Extra payment in summer and christmas vacations
        BigDecimal extra = BigDecimal.ZERO;
        if (payDate.getMonthValue() == 6 || payDate.getMonthValue() == 12) {
            extra = base;
        }
 
        long years    = ChronoUnit.YEARS.between(contract.startDate, payDate);
        long triennia = years / 3;
        BigDecimal triennium     = trienUnit.multiply(BigDecimal.valueOf(triennia))
                .setScale(SCALE, RM);
        BigDecimal productivity  = prodRate.multiply(invoiced).setScale(SCALE, RM);
        BigDecimal gross         = base.add(extra).add(productivity).add(triennium);
        BigDecimal tax           = taxRate.multiply(gross).setScale(SCALE, RM);
        
        // It is calculated BY MOTNH - FIX: nic discrenpancy
        BigDecimal monthly12     = annual.divide(BigDecimal.valueOf(12), 10, RM);
        BigDecimal nic           = monthly12.multiply(BigDecimal.valueOf(0.05))
                .setScale(SCALE, RM);
        BigDecimal totalDed      = tax.add(nic).setScale(SCALE, RM);
        
        // Net salary jsut up to a 2 decimals
        BigDecimal net           = gross.subtract(totalDed)
                .setScale(SCALE_NET_SALARY, RM);
 
        // Asignar al Record
        PayrollRecord r = new PayrollRecord();
        r.id                  = UUID.randomUUID().toString();
        r.version             = 1L;
        r.contractId          = contract.id;
        r.date                = payDate;
        r.baseSalary          = base.doubleValue();
        r.extraSalary         = extra.doubleValue();
        r.productivityEarning = productivity.doubleValue();
        r.trienniumEarning    = triennium.doubleValue();
        r.taxDeduction        = tax.doubleValue();
        r.nicDeduction        = nic.doubleValue();
        r.grossSalary         = gross.doubleValue();
        r.totalDeductions     = totalDed.doubleValue();
        r.netSalary           = net.doubleValue();
        r.createdAt           = LocalDateTime.now();
        r.updatedAt           = LocalDateTime.now();
 
        insertPayroll(r);
        return r;
    }
	
	private void insertPayroll(PayrollRecord r) {
	    Connection c = Jdbc.getCurrentConnection();
	    try (PreparedStatement pst = c.prepareStatement(
            Queries.getSQLSentence("TPAYROLLS_INSERT"))) {
 
            pst.setString(1, r.id);
            pst.setLong(2, r.version);
            pst.setString(3, r.contractId);
            pst.setDate(4, java.sql.Date.valueOf(r.date));
            pst.setBigDecimal(5,  BigDecimal.valueOf(r.baseSalary).setScale(SCALE, RM));
            pst.setBigDecimal(6,  BigDecimal.valueOf(r.extraSalary).setScale(SCALE, RM));
            pst.setBigDecimal(7,  BigDecimal.valueOf(r.productivityEarning).setScale(SCALE, RM));
            pst.setBigDecimal(8,  BigDecimal.valueOf(r.trienniumEarning).setScale(SCALE, RM));
            pst.setBigDecimal(9,  BigDecimal.valueOf(r.taxDeduction).setScale(SCALE, RM));
            pst.setBigDecimal(10, BigDecimal.valueOf(r.nicDeduction).setScale(SCALE, RM));
            pst.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pst.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            pst.setString(13, "ACTIVE");
 
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
	    }
	
	//Helpers
    private PayrollRecord toRecord(ResultSet rs) throws SQLException {
        PayrollRecord r = new PayrollRecord();
        r.id                  = rs.getString("ID");
        r.version             = rs.getLong("VERSION");
        r.contractId          = rs.getString("CONTRACT_ID");
        r.date                = rs.getDate("DATE").toLocalDate();
        r.baseSalary          = rs.getDouble("BASESALARY");
        r.extraSalary         = rs.getDouble("EXTRASALARY");
        r.productivityEarning = rs.getDouble("PRODUCTIVITYEARNING");
        r.trienniumEarning    = rs.getDouble("TRIENNIUMEARNING");
        r.taxDeduction        = rs.getDouble("TAXDEDUCTION");
        r.nicDeduction        = rs.getDouble("NICDEDUCTION");
        r.grossSalary         = r.baseSalary + r.extraSalary +
                                r.productivityEarning + r.trienniumEarning;
        r.totalDeductions     = r.taxDeduction + r.nicDeduction;
        r.netSalary           = r.grossSalary - r.totalDeductions;
        return r;
    }
 
    private PayrollSummaryRecord toSummaryRecord(ResultSet rs) throws SQLException {
        double base          = rs.getDouble("basesalary");
        double extra         = rs.getDouble("extrasalary");
        double productivity  = rs.getDouble("productivityearning");
        double triennium     = rs.getDouble("trienniumearning");
        double tax           = rs.getDouble("taxdeduction");
        double nic           = rs.getDouble("nicdeduction");
 
        PayrollSummaryRecord r = new PayrollSummaryRecord();
        r.id        = rs.getString("ID");
        r.date      = rs.getDate("DATE").toLocalDate();
        r.netSalary = (base + extra + productivity + triennium) - (tax + nic);
        return r;
    }

}