package uo.ri.cws.application.persistence.invoice.impl;

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
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class InvoiceGatewayImpl implements InvoiceGateway {
	
	@Override
    public void add(InvoiceRecord t) throws PersistenceException {
		
        Connection c = Jdbc.getCurrentConnection();
        
        //Record should be an exact copy of the table.
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_INSERT"))) {
 
            pst.setString(1, t.id);
            pst.setLong(2, t.number);
            pst.setDate(3, java.sql.Date.valueOf(t.date));
            pst.setDouble(4, t.vat);
            pst.setDouble(5, t.amount);
            pst.setString(6, "NOT_YET_PAID");
            pst.setLong(7, 1L);
            pst.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pst.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            pst.setString(10, "ENABLED");
 
            int updated = pst.executeUpdate();
            if (updated != 1) {
                throw new PersistenceException(
                        "Invoice insert failed, rows affected: " + updated);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }


	@Override
	public void remove(String id) throws PersistenceException {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(InvoiceRecord t) throws PersistenceException {
		// TODO Auto-generated method stub
	}


	@Override
    public Optional<InvoiceRecord> findById(String id) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FIND_BY_ID"))) {
 
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
    public List<InvoiceRecord> findAll() throws PersistenceException {
        List<InvoiceRecord> out = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FIND_ALL"));
             ResultSet rs = pst.executeQuery()) {
 
            while (rs.next()) {
                out.add(toRecord(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
        return out;
    }

	/* Much better do both things via WorkOrder Gateway
	 * 
	@Override
	public Optional<InvoiceRecord> findByNif(String nif) 
	throws PersistenceException {

		InvoiceRecord rec = new InvoiceRecord();
		Connection c = Jdbc.getCurrentConnection();

		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_FIND_BY_NIF"))) {
	    	pst.setString(1, nif);
			try(ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					rec.id = rs.getString("ID");
				    //rec.number = rs.getLong("number"); not yet
				    //rec.vat = rs.getDouble("vat");
					rec.date = rs.getDate("date").toLocalDate();
					rec.amount = rs.getDouble("amount");
					rec.state = rs.getString("state"); 
					rec.version = rs.getLong("version");
				}else {
					return Optional.empty();
				}
			}
		} catch (SQLException e) {
		    throw new PersistenceException(e);
		}
		return Optional.of(rec);
	}

	@Override
	public List<InvoicingWorkOrderRecord> findNotInvoicedWorkOrdersByClientNif(
			String nif) throws PersistenceException {
		
	    List<InvoicingWorkOrderRecord> invoicingWorkOrderDtos = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c
                .prepareStatement(Queries.getSQLSentence(
                		"TWORKORDERS_FIND_NOT_INVOICED"))) {
            pst.setString(1, nif);
            try (ResultSet rs = pst.executeQuery();) {
                while (rs.next()) {
                	InvoicingWorkOrderRecord dto = new InvoicingWorkOrderRecord();
                	dto.id = rs.getString("ID");
                	dto.state = rs.getString("STATE");
                	//Converts date to LocalDateTime
                	dto.date = rs.getTimestamp("DATE").toLocalDateTime();
                	dto.description = rs.getString("DESCRIPTION");
                	dto.amount = rs.getDouble("AMOUNT");
                	invoicingWorkOrderDtos.add(dto);
                }
            } 
        } catch (SQLException e) {
        	throw new PersistenceException(e);
        }
        return invoicingWorkOrderDtos;
	} */

    @Override
    public long findNextNumber() throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_SELECT_LAST_NUMBER"));
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                long val = rs.getLong(1);
                return rs.wasNull() ? 1L : val + 1L;
            }
            return 1L;
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }

    /* No hace falta
    public Optional<InvoiceRecord> findByNumber(long number) throws PersistenceException {
        Connection c = Jdbc.getCurrentConnection();
        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TINVOICES_FIND_BY_NUMBER"))) {
 
            pst.setLong(1, number);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(toRecord(rs));
            }
        } catch (SQLException e) {
            throw new PersistenceException(e);
        }
    }*/
    
    
    //Helper
    private InvoiceRecord toRecord(ResultSet rs) throws SQLException {
        InvoiceRecord r = new InvoiceRecord();
        r.id      = rs.getString("ID");
        r.number  = rs.getLong("NUMBER");
        r.date    = rs.getDate("DATE").toLocalDate();
        r.vat     = rs.getDouble("VAT");
        r.amount  = rs.getDouble("AMOUNT");
        r.state   = rs.getString("STATE");
        r.version = rs.getLong("VERSION");
        return r;
    }

}
