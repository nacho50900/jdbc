package uo.ri.cws.application.persistence.workorder.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class WorkOrderGatewayImpl implements WorkOrderGateway {

    @Override
    public void add(WorkorderRecord t) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TWORKORDERS_ADD"))) {
	    pst.setString(1, t.id);
	    pst.setString(2, t.description);
	    pst.setTimestamp(3, Timestamp.valueOf(t.date));
	    pst.setString(4, t.state);
	    pst.setDouble(5, t.amount);
	    pst.setLong(6, t.version);
	    pst.setTimestamp(7, Timestamp.valueOf(t.createdAt));
	    pst.setTimestamp(8, Timestamp.valueOf(t.updatedAt));
	    pst.setString(9, t.entityState);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
    }

    @Override
    public void remove(String id) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TWORKORDERS_DELETE"))) {
	    pst.setString(1, id);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
    }

    @Override
    public void update(WorkorderRecord t) throws PersistenceException {
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TWORKORDERS_UPDATE"))) {
	    pst.setString(1, t.description);
	    pst.setTimestamp(2, Timestamp.valueOf(t.date));
	    pst.setString(3, t.state);
	    pst.setDouble(4, t.amount);
	    pst.setString(5, t.invoiceId);
	    pst.setString(6, t.mechanicId);
	    pst.setString(7, t.vehicleId);
	    pst.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
	    pst.setString(9, t.id);
	    pst.executeUpdate();
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
    }

    @Override
    public Optional<WorkorderRecord> findById(String id)
	throws PersistenceException {
	Optional<WorkorderRecord> wr = Optional.empty();
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TWORKORDERS_FIND_BY_ID"))) {
	    pst.setString(1, id);
	    try (ResultSet rs = pst.executeQuery()) {
		if (rs.next()) {
		    wr = Optional.of(toRecord(rs));
		}
	    }
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
	return wr;
    }

    @Override
    public List<WorkorderRecord> findAll() throws PersistenceException {
	List<WorkorderRecord> list = new ArrayList<>();
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c
	    .prepareStatement(Queries.getSQLSentence("TWORKORDERS_FINDALL"))) {
	    try (ResultSet rs = pst.executeQuery()) {
		while (rs.next()) {
		    list.add(toRecord(rs));
		}
	    }
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
	return list;
    }

    @Override
    public List<WorkorderRecord> findNotInvoicedWorkOrdersByNif(String nif) {
	List<WorkorderRecord> i = new ArrayList<>();
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c.prepareStatement(
	    Queries.getSQLSentence("TWORKORDERS_FIND_NOT_INVOICED"))) {
	    pst.setString(1, nif);
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

    @Override
    public List<WorkorderRecord> findByMechanicId(String id) {
	List<WorkorderRecord> w = new ArrayList<>();
	Connection c = Jdbc.getCurrentConnection();
	try (PreparedStatement pst = c.prepareStatement(
	    Queries.getSQLSentence("TWORKORDERS_FIND_BY_MECHANIC_ID"))) {
	    pst.setString(1, id);
	    try (ResultSet rs = pst.executeQuery()) {
		while (rs.next()) {
		    w.add(toRecord(rs));
		}
	    }
	} catch (SQLException e) {
	    throw new PersistenceException(e);
	}
	return w;
    }
    
    //Helper
    static WorkorderRecord toRecord(ResultSet rs) throws SQLException {
    	WorkorderRecord wR = new WorkorderRecord();

    	wR.id = rs.getString("ID");
    	wR.description = rs.getString("DESCRIPTION");

    	Timestamp tsDate = rs.getTimestamp("DATE");
    	if (tsDate != null) {
    	    wR.date = tsDate.toLocalDateTime();
    	} else {
    	    wR.date = null;
    	}

    	wR.state = rs.getString("STATE");
    	wR.amount = rs.getDouble("AMOUNT");

    	wR.invoiceId = rs.getString("INVOICE_ID");
    	wR.mechanicId = rs.getString("MECHANIC_ID");
    	wR.vehicleId = rs.getString("VEHICLE_ID");

    	wR.version = rs.getLong("VERSION");

    	Timestamp tsCreated = rs.getTimestamp("CREATEDAT");
    	if (tsCreated != null) {
    	    wR.createdAt = tsCreated.toLocalDateTime();
    	} else {
    	    wR.createdAt = null;
    	}

    	Timestamp tsUpdated = rs.getTimestamp("UPDATEDAT");
    	if (tsUpdated != null) {
    	    wR.updatedAt = tsUpdated.toLocalDateTime();
    	} else {
    	    wR.updatedAt = null;
    	}

    	wR.entityState = rs.getString("ENTITYSTATE");
    	return wR;
        }

}