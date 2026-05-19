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
import uo.ri.cws.application.persistence.invoice.InvoiceAssembler;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class InvoiceGatewayImpl implements InvoiceGateway {

	@Override
	public void add(InvoiceRecord t) throws PersistenceException {
		
	    InvoiceDto dto = InvoiceAssembler.toDto(t);
	    //Record should be an exact copy of the table.
	    //List<String> workOrderIds = null;//= 
	    //instanciateWorkOrdersIdsById(t.id); ITS GIVEN
		Connection c = Jdbc.getCurrentConnection();

		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TINVOICES_INSERT"))) {
		    pst.setString(1, dto.id);
		    pst.setLong(2, dto.number);
		    pst.setDate(3, java.sql.Date.valueOf(dto.date));
		    pst.setDouble(4, dto.vat);
		    pst.setDouble(5, dto.amount);
		    pst.setString(6, "NOT_YET_PAID");
		    pst.setLong(7, 1L);
		    //createdAt
			pst.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
			//updatedAt
			pst.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
			pst.setString(10, "ENABLED"); // entityState

            int updated = pst.executeUpdate();
            if (updated != 1) {
                throw new PersistenceException(
                		"Invoice insert failed, rows affected: " + updated);
            }
		} catch (SQLException e) {
		    throw new RuntimeException();
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
	public Optional<InvoiceRecord> findById(String id) 
			throws PersistenceException {
		
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                 Queries.getSQLSentence("TWORKORDERS_FIND_BY_ID"))) {
            pst.setString(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
					return Optional.empty();
				}
                InvoiceDto dto = new InvoiceDto();
                dto.id = rs.getString("id");
                dto.number = rs.getLong("number");
                dto.date = rs.getDate("date").toLocalDate();
                dto.vat = rs.getDouble("vat");
                dto.amount = rs.getDouble("amount");
                dto.state = rs.getString("state");   // o status
                dto.version = rs.getLong("version");
                return Optional.of(InvoiceAssembler.toRecord(dto));
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
                InvoiceDto dto = new InvoiceDto();
                dto.id      = rs.getString("id");
                dto.number  = rs.getLong("number");
                dto.date    = rs.getDate("date").toLocalDate();
                dto.vat     = rs.getDouble("vat");
                dto.amount  = rs.getDouble("amount");
                dto.state   = rs.getString("state"); // o "status"
                dto.version = rs.getLong("version");
                out.add(InvoiceAssembler.toRecord(dto));
            }
	        return out;
	    } catch (SQLException e) {
	        throw new PersistenceException(e);
	    }
	}

	
	@Override
	public Optional<InvoiceRecord> findByNif(String nif) {
    	//Process
		InvoiceDto dto = new InvoiceDto();
		Connection c = Jdbc.getCurrentConnection();

		try (PreparedStatement pst = c.prepareStatement(
				Queries.getSQLSentence("TWORKORDERS_FIND_BY_NIF"))) {
	    	pst.setString(1, nif);
			try(ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					dto.id = rs.getString("ID");
				    //dto.number = rs.getLong("number"); not yet
				    //dto.vat = rs.getDouble("vat");
				    dto.date = rs.getDate("date").toLocalDate();
				    dto.amount = rs.getDouble("amount");
				    dto.state = rs.getString("state"); 
				    dto.version = rs.getLong("version");
				}else {
					return Optional.empty();
				}
			}
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}
		return Optional.of(InvoiceAssembler.toRecord(dto));
	}
	
	@Override
	public List<InvoicingWorkOrderDto> findNotInvoicedWorkOrdersByClientNif(
			String nif) throws BusinessException {
		
	    List<InvoicingWorkOrderDto> InvoicingWorkOrderDtos = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c
                .prepareStatement(Queries.getSQLSentence(
                		"TWORKORDERS_FIND_NOT_INVOICED"))) {
            pst.setString(1, nif);
            try (ResultSet rs = pst.executeQuery();) {
                while (rs.next()) {
                	InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
                	dto.id = rs.getString("ID");
                	dto.state = rs.getString("STATE");
                	//Converts date to LocalDateTime
                	dto.date = rs.getTimestamp("DATE").toLocalDateTime();
                	dto.description = rs.getString("DESCRIPTION");
                	dto.amount = rs.getDouble("AMOUNT");
                    InvoicingWorkOrderDtos.add(dto);
                }
            } 
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        }
        return InvoicingWorkOrderDtos;
	}
}
