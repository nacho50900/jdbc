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
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class MechanicGatewayImpl implements MechanicGateway {

    @Override
    public void add(MechanicRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_ADD"))) {

            pst.setString(1, t.id);
            pst.setString(2, t.nif);
            pst.setString(3, t.name);
            pst.setString(4, t.surname);
            pst.setLong(5, t.version);
            pst.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            pst.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            pst.setString(8, "ACTIVE");

            pst.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Error inserting mechanic: " + t.nif, e);
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
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(MechanicRecord t) throws PersistenceException {
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
                throw new PersistenceException(
                        "Optimistic lock failed (id=" + t.id
                        + ", version=" + t.version + ")");
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
    public Optional<MechanicRecord> findByNif(String nif)
            throws PersistenceException {
        return findBy(nif, Queries.getSQLSentence("TMECHANICS_FIND_BY_NIF"));
    }

    @Override
    public List<MechanicRecord> findAll() throws PersistenceException {
        List<MechanicRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TMECHANICS_FINDALL"))) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(toRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public List<MechanicRecord> findMechanicsWithValidContract()
            throws PersistenceException {
        List<MechanicRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                        "TMECHANICS_FINDMECHANICSWITHVALIDCONTRACT"))) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(toRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return result;
    }

    @Override
    public boolean hasWorkOrders(String mechanicId)
            throws PersistenceException {
        return countBy("TMECHANICS_HAS_WORKORDERS", mechanicId) > 0;
    }

    @Override
    public boolean hasInterventions(String mechanicId)
            throws PersistenceException {
        return countBy("TMECHANICS_HAS_INTERVENTIONS", mechanicId) > 0;
    }

    @Override
    public boolean hasContracts(String mechanicId)
            throws PersistenceException {
        return countBy("TMECHANICS_HAS_CONTRACT", mechanicId) > 0;
    }

    // Helpers
    private Optional<MechanicRecord> findBy(String value, String sql)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setString(1, value);
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

    private long countBy(String queryKey, String id)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(queryKey))) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0L;
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    private MechanicRecord toRecord(ResultSet rs) throws SQLException {
        MechanicRecord r = new MechanicRecord();
        r.id      = rs.getString("ID");
        r.nif     = rs.getString("NIF");
        r.name    = rs.getString("NAME");
        r.surname = rs.getString("SURNAME");
        r.version = rs.getLong("VERSION");
        return r;
    }
}