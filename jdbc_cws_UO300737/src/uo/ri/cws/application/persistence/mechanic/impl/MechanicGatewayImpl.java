package uo.ri.cws.application.persistence.mechanic.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.mechanic.MechanicAssembler;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class MechanicGatewayImpl implements MechanicGateway {	

	@Override
	public void add(MechanicRecord t) throws PersistenceException {
		
		//Process
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                 Queries.getSQLSentence("TMECHANICS_ADD"))) {
        	
            pst.setString(1, t.id);
            pst.setString(2, t.nif);
            pst.setString(3, t.name);
            pst.setString(4, t.surname);
            pst.setLong(5, t.version);
            pst.setTimestamp(6, java.sql.Timestamp.valueOf(t.createdAt));
            pst.setTimestamp(7, java.sql.Timestamp.valueOf(t.updateAt));
            pst.setString(8, t.entityState);

            pst.executeUpdate();
        
	    } catch (SQLException e) {
	        throw new RuntimeException("Error inserting mechanic: " + t.nif, e);
	    }
	}

    @Override
    public void remove(String id) throws PersistenceException {
		
        Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TMECHANICS_DELETE"))) {
			pst.setString(1, id);
			pst.executeUpdate();
	    
	
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
    }
    
    @Override
    public void update(MechanicRecord t) throws PersistenceException {
    	
	    // Try with resources eliminated because of avoid closing the current
	    // conection, all the same conection in order top assure transaction
    	
        Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TMECHANICS_UPDATE"))) {

			pst.setString(1, t.name);
			pst.setString(2, t.surname);
			pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			pst.setString(4, t.id);
			pst.setLong(5, t.version);
			
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
    public Optional<MechanicRecord> findById(String id)
	throws PersistenceException {
    	return findBy(id, Queries.getSQLSentence("TMECHANICS_FIND_BY_ID"));
    }
    
    @Override
    public List<MechanicRecord> findAll() throws PersistenceException {
    	
		List<MechanicRecord> mechanicsList = new ArrayList<MechanicRecord>();
	    Connection c = Jdbc.getCurrentConnection();

	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TMECHANICS_FINDALL"))) {
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
				    MechanicDto dto = new MechanicDto();
				    //get(NAME) Safer and robust than indexes
				    dto.nif = rs.getString("NIF");
				    dto.name = rs.getString("NAME");
				    dto.surname = rs.getString("SURNAME");
				    dto.id = rs.getString("ID");
				    dto.version = rs.getLong("VERSION");
				    mechanicsList.add(MechanicAssembler.toRecord(dto));
				}
		    }
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return mechanicsList;
    }

    @Override
    public Optional<MechanicRecord> findByNif(String nif) {
    	return findBy(nif, Queries.getSQLSentence("TMECHANICS_FINDBYNIF"));
    }

    public Optional<MechanicRecord> findBy(String IdOrNif,
	String selectedAssignText) {
    	
		// Process
		MechanicDto dto = new MechanicDto();
		Connection c = Jdbc.getCurrentConnection();
		try {
	        if (c == null || c.isClosed()) { 
	            c = Jdbc.createThreadConnection(); 
	        } //Need it because Crud Service use it first
		    try (PreparedStatement pst = c.prepareStatement(
				selectedAssignText)) {
				pst.setString(1, IdOrNif);
				try (ResultSet rs = pst.executeQuery()) {
					if (rs.next()) {
					    dto.nif = rs.getString("NIF");
					    dto.name = rs.getString("NAME");
					    dto.surname = rs.getString("SURNAME");
					    dto.id = rs.getString("ID");
					    dto.version = rs.getLong("VERSION");
					} else {
					    return Optional.empty();
					}
				}
		    }
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return Optional.of(MechanicAssembler.toRecord(dto));
    }

	@Override
	public List<MechanicRecord> findMechanicsWithValidContract() {

		List<MechanicRecord> mechanicsList = new ArrayList<MechanicRecord>();
        Connection c = Jdbc.getCurrentConnection();
        
	    try (PreparedStatement pst = c.prepareStatement(
		Queries.getSQLSentence("TMECHANICS_FINDMECHANICSWITHVALIDCONTRACT"))) {
			try (ResultSet rs = pst.executeQuery()) {
				while (rs.next()) {
				    MechanicDto dto = new MechanicDto();
				    dto.nif = rs.getString("NIF");
				    dto.name = rs.getString("NAME");
				    dto.surname = rs.getString("SURNAME");
				    dto.id = rs.getString("ID");
				    dto.version = rs.getLong("VERSION");
				    mechanicsList.add(MechanicAssembler.toRecord(dto));
				}
			}
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return mechanicsList;
	}
}