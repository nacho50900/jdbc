package uo.ri.cws.application.persistence.professionalgroup.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class ProfessionalGroupGatewayImpl implements ProfessionalGroupGateway {

	@Override
	public void add(ProfessionalGroupRecord t) throws PersistenceException {
	  
		Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                 Queries.getSQLSentence("TPROFESSIONALGROUPS_ADD"))) {
        	
            pst.setString(1, t.id);
            pst.setString(2, t.name);
            pst.setDouble(3, t.productivityRate);
            pst.setDouble(4, t.trienniumPayment);
            pst.setLong(5, t.version);
            pst.setTimestamp(6, java.sql.Timestamp.valueOf(t.createdAt));
            pst.setTimestamp(7, java.sql.Timestamp.valueOf(t.updateAt));

            pst.executeUpdate();
	    } catch (SQLException e) {
	        throw new PersistenceException("Error inserting Professional Group: " 
	        		+ t.name, e);
	    }
	}

	@Override
	public void remove(String id) throws PersistenceException {
		
    	Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TPROFESSIONALGROUPS_DELETE"))) {
			pst.setString(1, id);
			pst.executeUpdate();
		} catch (SQLException e) {
		    throw new PersistenceException(e);
		}
	}

	@Override
	public void update(ProfessionalGroupRecord t) throws PersistenceException {
		
		Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TPROFESSIONALGROUPS_UPDATE"))) {

			pst.setString(1, t.name);
			pst.setDouble(2, t.trienniumPayment);
			pst.setDouble(3, t.productivityRate);
			pst.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pst.setString(5, t.id);
			pst.setLong(6, t.version);
			
			int updated = pst.executeUpdate();

			if (updated == 0) {
                // No coincide la versión o no existe el id
                throw new PersistenceException("Optimistic lock failed (id=" +
                		t.id + ", version=" + t.version + ")");
            }
				t.version = t.version + 1;
		} catch (SQLException e) {
		    throw new PersistenceException(e);
		}
	}

	@Override
    public Optional<ProfessionalGroupRecord> findById(String id)
            throws PersistenceException {
 
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPROFESSIONALGROUPS_FIND_BY_ID"))) {
 
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
    public Optional<ProfessionalGroupRecord> findByName(String name)
            throws PersistenceException {
 
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPROFESSIONALGROUPS_FIND_BY_NAME"))) {
 
            pst.setString(1, name);
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
    public List<ProfessionalGroupRecord> findAll() throws PersistenceException {
        List<ProfessionalGroupRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TPROFESSIONALGROUPS_FINDALL"));
             ResultSet rs = pst.executeQuery()) {
 
            while (rs.next()) {
                result.add(toRecord(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }
    
    public boolean hasContracts(String groupId) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_COUNT_BY_PROFESSIONALGROUP_ID"))) {
 
            pst.setString(1, groupId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return false;
    }
 
    // HELPER
    private ProfessionalGroupRecord toRecord(ResultSet rs) throws SQLException {
        ProfessionalGroupRecord r = new ProfessionalGroupRecord(
                rs.getString("NAME"),
                rs.getDouble("TRIENNIUMPAYMENT"),
                rs.getDouble("PRODUCTIVITYRATE"));
        r.id      = rs.getString("ID");
        r.version = rs.getLong("VERSION");
        return r;
    }

}
