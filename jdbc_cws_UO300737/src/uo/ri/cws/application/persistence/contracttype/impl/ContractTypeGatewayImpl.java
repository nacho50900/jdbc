package uo.ri.cws.application.persistence.contracttype.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contracttype.ContractTypeGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class ContractTypeGatewayImpl implements ContractTypeGateway {

    @Override
    public void add(ContractTypeRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_INSERT"))) {

            pst.setString(1, t.id);
            pst.setString(2, t.name);
            pst.setDouble(3, t.compensationDays);
            pst.setLong(4, t.version);
            pst.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pst.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_DELETE"))) {

            pst.setString(1, id);
            pst.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void update(ContractTypeRecord t) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_UPDATE"))) {

            pst.setDouble(1, t.compensationDays);
            pst.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pst.setString(3, t.id);
            pst.setLong(4, t.version);

            int updated = pst.executeUpdate();
            if (updated == 0) {
                throw new PersistenceException(
                        "Optimistic lock failed for ContractType id="
                        + t.id + " version=" + t.version);
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ContractTypeRecord> findById(String id)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_FIND_BY_ID"))) {

            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Optional<ContractTypeRecord> findByName(String name)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_FIND_BY_NAME"))) {

            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<ContractTypeRecord> findAll() throws PersistenceException {
        List<ContractTypeRecord> result = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_FIND_ALL"));
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public int countContractsFor(String contractTypeId)
            throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTTYPES_COUNT_CONTRACTS"))) {

            pst.setString(1, contractTypeId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    // ---------------------------------------------------------------

    private ContractTypeRecord mapRow(ResultSet rs) throws SQLException {
        ContractTypeRecord r = new ContractTypeRecord();
        r.id = rs.getString("ID");
        r.version = rs.getLong("VERSION");
        r.name = rs.getString("NAME");
        r.compensationDays = rs.getDouble("COMPENSATIONDAYSPERYEAR");
        return r;
    }

}
