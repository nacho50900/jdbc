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
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
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
	        throw new RuntimeException("Error inserting Professional Group: " 
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
		    throw new RuntimeException(e);
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

		ProfessionalGroupDto dto = new ProfessionalGroupDto();
		Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
	    		Queries.getSQLSentence("TPROFESSIONALGROUPS_FIND_BY_ID"))) {
			pst.setString(1, id);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
				    dto.id = rs.getString("ID");
				    dto.name = rs.getString("NAME");
				    dto.trienniumPayment = rs.getDouble("TRIENNIUMPAYMENT");
				    dto.productivityRate = rs.getDouble("PRODUCTIVITYRATE");
				    dto.version = rs.getLong("VERSION");
				} else {
				    return Optional.empty();
				}
			}
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return Optional.of(ProfessionalGroupAssembler.toRecord(dto));
	}
	
    @Override
	public Optional<ProfessionalGroupRecord> findByName(String name) {

		ProfessionalGroupDto dto = new ProfessionalGroupDto();
		Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
	    		Queries.getSQLSentence("TPROFESSIONALGROUPS_FIND_BY_NAME"))) {
			pst.setString(1, name);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
				    dto.id = rs.getString("ID");
				    dto.name = rs.getString("NAME");
				    dto.trienniumPayment = rs.getDouble("TRIENNIUMPAYMENT");
				    dto.productivityRate = rs.getDouble("PRODUCTIVITYRATE");
				    dto.version = rs.getLong("VERSION");
				} else {
				    return Optional.empty();
				}
			}
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return Optional.of(ProfessionalGroupAssembler.toRecord(dto));
	}	

	@Override
	public List<ProfessionalGroupRecord> findAll() throws PersistenceException {
		List<ProfessionalGroupRecord> professionalGroupList = 
				new ArrayList<ProfessionalGroupRecord>();
		Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TPROFESSIONALGROUPS_FINDALL"))) {
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
					ProfessionalGroupDto dto = new ProfessionalGroupDto();
				    dto.id = rs.getString("ID"); //Safer and robust than indexes
				    dto.name = rs.getString("NAME");
				    dto.trienniumPayment = rs.getDouble("TRIENNIUMPAYMENT");
				    dto.productivityRate = rs.getDouble("PRODUCTIVITYRATE");
				    dto.version = rs.getLong("VERSION");
				    professionalGroupList.add(
				    		ProfessionalGroupAssembler.toRecord(dto));
				}
			}
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return professionalGroupList;
	}

}
