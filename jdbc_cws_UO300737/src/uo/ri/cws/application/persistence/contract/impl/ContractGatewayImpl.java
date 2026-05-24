package uo.ri.cws.application.persistence.contract.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractSummaryAssembler;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class ContractGatewayImpl implements ContractGateway{

	@Override
	public void add(ContractRecord t) throws PersistenceException {

        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                 Queries.getSQLSentence("TCONTRACTS_INSERT"))) {
        	
            pst.setString(1, t.id);
            pst.setString(2, t.mechanicId);
            pst.setString(3, t.contractTypeId);
            pst.setString(4, t.professionalGroupId);
            pst.setLong(5, t.version);
            pst.setDate(6, Date.valueOf(t.startDate));
            if (t.endDate != null) {
                pst.setDate(7, Date.valueOf(t.endDate));
            } else {
            	pst.setDate(7, null);
            }
            
            pst.setDouble(8, t.annualBaseSalary);
            pst.setDouble(9, t.taxRate);
            
            pst.setDouble(10, t.settlement);
            pst.setString(11, t.state);
            
            pst.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            pst.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));

            pst.executeUpdate();
        
	    } catch (SQLException e) {
	        throw new PersistenceException("Error inserting contract: " + t.id, e);
	    }
	}

	@Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_DELETE"))) {

            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ContractRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_UPDATE"))) {

            pst.setDate(1,
                    t.endDate != null
                    ? java.sql.Date.valueOf(t.endDate) : null);
            pst.setDouble(2, t.annualBaseSalary);
            pst.setDouble(3, t.settlement);
            pst.setString(4, t.state);
            pst.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pst.setString(6, t.id);
            pst.setLong(7, t.version);

            int updated = pst.executeUpdate();
            if (updated == 0) {
                throw new PersistenceException(
                        "Optimistic lock failed for Contract id="
                        + t.id + " version=" + t.version);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ContractRecord> findById(String id)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_FIND_BY_ID"))) {

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
    public List<ContractRecord> findAll() throws PersistenceException {
    	List<ContractRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_FIND_ALL"));
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                result.add(toRecord(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

	@Override
	public List<ContractSummaryRecord> findByMechanicNif(String nif) 
			throws PersistenceException {

		List<ContractSummaryRecord> contracts = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                		"TCONTRACTS_FIND_BY_MECHANIC_NIF"))) {
            pst.setString(1, nif);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    ContractSummaryDto dto = new ContractSummaryDto();
                    dto.id = rs.getString("id");
                    dto.nif = rs.getString("nif");
                    dto.settlement = rs.getDouble("settlement");
                    dto.state = rs.getString("state");
                    dto.numPayrolls = rs.getInt("numPayrolls");

                    contracts.add(ContractSummaryAssembler.toRecord(dto));
                }
            }
	    } catch(SQLException e) {
	    	 throw new PersistenceException(e.getMessage());
	    }
	    return contracts;
    }

	@Override
	public List<ContractRecord> findInForceContracts() 
			throws PersistenceException {
		
	    List<ContractRecord> contracts = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_FIND_ALL_INFORCE"))) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    contracts.add(toRecord(rs));
                }
	        }
	    } catch (SQLException e) {
	        throw new PersistenceException(e.getMessage());
	    }
	    return contracts;
	}

    public boolean hasPayrolls(String contractId)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_COUNT_PAYROLLS"))) {

            pst.setString(1, contractId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    public boolean mechanicHasWorkOrders(String mechanicId)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_HAS_WORKORDERS"))) {

            pst.setString(1, mechanicId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }
    
    public Optional<ContractRecord> findInForceByMechanicId(
            String mechanicId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                        "TCONTRACTS_FIND_INFORCE_BY_MECHANIC_ID"))) {

            pst.setString(1, mechanicId);
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
    
    
    // HELPER
    private ContractRecord toRecord(ResultSet rs) throws SQLException {
        ContractRecord r = new ContractRecord();
        r.id = rs.getString("ID");
        r.version = rs.getLong("VERSION");
        r.mechanicId = rs.getString("MECHANIC_ID");
        r.contractTypeId = rs.getString("CONTRACTTYPE_ID");
        r.professionalGroupId = rs.getString("PROFESSIONALGROUP_ID");
        r.startDate = rs.getDate("STARTDATE").toLocalDate();

        java.sql.Date end = rs.getDate("ENDDATE");
        r.endDate = end != null ? end.toLocalDate() : null;

        r.annualBaseSalary = rs.getDouble("ANNUALBASESALARY");
        r.taxRate = rs.getDouble("TAXRATE");
        r.settlement = rs.getDouble("SETTLEMENT");
        r.state = rs.getString("STATE");
        return r;
    }

}

