package uo.ri.cws.application.persistence.intervention.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.intervention.InterventionGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class InterventionGatewayImpl implements InterventionGateway {

    @Override
    public void add(InterventionRecord t) throws PersistenceException {
	// TODO Auto-generated method stub

    }

    @Override
    public void remove(String id) throws PersistenceException {
	// TODO Auto-generated method stub

    }

    @Override
    public void update(InterventionRecord t) throws PersistenceException {
	// TODO Auto-generated method stub

    }

    @Override
    public Optional<InterventionRecord> findById(String id)
	throws PersistenceException {
	// TODO Auto-generated method stub
	return Optional.empty();
    }

    @Override
    public List<InterventionRecord> findAll() throws PersistenceException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<InterventionRecord> findByMechanicId(String id)
	throws PersistenceException {
	// TODO Auto-generated method stub
	List<InterventionRecord> i = new ArrayList<InterventionRecord>();
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c.prepareStatement(
	    Queries.getSQLSentence("TINTERVENTION_FIND_BY_MECHANIC_ID"))) {
	    pst.setString(1, id);
	    try (ResultSet rs = pst.executeQuery()) {
		while (rs.next()) {
		    i.add(toRecord(rs));
		}
	    }

	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
	return i;
    }

    //Helper
    static InterventionRecord toRecord(ResultSet rs)
    		throws SQLException {
		InterventionRecord iR = new InterventionRecord();
		iR.id = rs.getString("id");
		iR.version = rs.getLong("version");
		iR.minutes = rs.getInt("minutes");

		Timestamp tsDate = rs.getTimestamp("date");
		iR.date = (tsDate != null) ? tsDate.toLocalDateTime() : null;

		iR.mechanicId = rs.getString("mechanic_Id");
		iR.workOrderId = rs.getString("workorder_Id");
		iR.createdAt = rs.getTimestamp("createdAt").toLocalDateTime();
		iR.updatedAt = rs.getTimestamp("updatedAt").toLocalDateTime();
		iR.entityState = rs.getString("entityState");
		return iR;
    }
}
