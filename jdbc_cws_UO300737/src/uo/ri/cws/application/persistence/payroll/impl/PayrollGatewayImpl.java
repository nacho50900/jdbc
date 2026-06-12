package uo.ri.cws.application.persistence.payroll.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class PayrollGatewayImpl implements PayrollGateway {

    @Override
    public void add(PayrollRecord t) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TPAYROLLS_ADD"))) {
	    pst.setString(1, t.id);
	    pst.setLong(2, t.version);
	    pst.setString(3, t.contractId);
	    pst.setDate(4, Date.valueOf(t.date));
	    pst.setDouble(5, t.baseSalary);
	    pst.setDouble(6, t.extraSalary);
	    pst.setDouble(7, t.productivityEarning);
	    pst.setDouble(8, t.trienniumEarning);
	    pst.setDouble(9, t.taxDeduction);
	    pst.setDouble(10, t.nicDeduction);
	    pst.setTimestamp(11, Timestamp.valueOf(t.createdAt));
	    pst.setTimestamp(12, Timestamp.valueOf(t.updatedAt));
	    pst.setString(13, t.entityState);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
    }

    @Override
    public void remove(String id) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TPAYROLLS_DELETE"))) {
	    pst.setString(1, id);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
    }

    @Override
    public void update(PayrollRecord t) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TPAYROLLS_UPDATE"))) {
	    pst.setDouble(1, t.baseSalary);
	    pst.setDouble(2, t.extraSalary);
	    pst.setDouble(3, t.productivityEarning);
	    pst.setDouble(4, t.trienniumEarning);
	    pst.setDouble(5, t.taxDeduction);
	    pst.setDouble(6, t.nicDeduction);
	    pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
	    pst.setString(8, t.id);
	    pst.setLong(9, t.version);
	    int updated = pst.executeUpdate();
	    if (updated == 0) {
		throw new PersistenceException(
		    "Error updating payroll (version check)");
	    }
	    t.version = t.version + 1;
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
    public List<PayrollRecord> findByContractId(String id)
	throws PersistenceException {
		List<PayrollRecord> pl = new ArrayList<>();
		Connection c = Jdbc.getCurrentConnection();
		try (PreparedStatement pst = c.prepareStatement(
		    Queries.getSQLSentence("TPAYROLLS_FIND_BY_CONTRACT_ID"))) {
		    pst.setString(1, id);
		    try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
				    pl.add(toRecord(rs));
				}
		    }
		} catch (SQLException e) {
		    throw new PersistenceException(e);
		}
		return pl;
    }

    /*
    @Override
    public double findSumOfLast12GrossesByContractId(String contractId)
	throws PersistenceException {
	double totalGross = 0.0;
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c.prepareStatement(Queries
	    .getSQLSentence("TPAYROLLS_FIND_LAST_12_GROSSES_BY_CONTRACT_ID"))) {
	    pst.setString(1, contractId);
	    try (ResultSet rs = pst.executeQuery()) {
		while (rs.next()) {
		    totalGross += rs.getDouble("calculatedGross");
		}
	    }
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
	return totalGross;
    }*/
  
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