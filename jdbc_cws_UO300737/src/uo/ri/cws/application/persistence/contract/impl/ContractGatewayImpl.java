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
import java.util.UUID;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractSummaryAssembler;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.exception.BusinessException;
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
	        throw new RuntimeException("Error inserting contract: " + t.id, e);
	    }
	}

	@Override
	public void remove(String id) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ContractRecord t) throws PersistenceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Optional<ContractRecord> findById(String id) 
			throws PersistenceException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<ContractRecord> findAll() throws PersistenceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<List<ContractSummaryRecord>> findByMechanicNif(String nif) 
			throws BusinessException {

		List<ContractSummaryRecord> contracts = new ArrayList<>();
        Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence(
                		"TCONTRACTS_FIND_INFORCE_BY_MECHANIC_NIF"))) {
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
	    	 throw new BusinessException(e.getMessage());
	    }
	    if (contracts.isEmpty()) {
	        return Optional.empty();
	    }
	    return Optional.of(contracts);
    }

	@Override
	public Optional<List<ContractRecord>> findInForceContracts() 
			throws BusinessException {
		
	    List<ContractRecord> contracts = new ArrayList<>();
	    Connection c = Jdbc.getCurrentConnection();

        try (PreparedStatement pst = c.prepareStatement(
                Queries.getSQLSentence("TCONTRACTS_FIND_ALL_INFORCE"))) {

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                	ContractRecord r = new ContractRecord();
                    r.id = UUID.randomUUID().toString();
                    r.version = 1L;
                    
                    r.mechanicId = rs.getString("mechanic_id");
					r.contractTypeId = rs.getString("contracttype_id");
					r.professionalGroupId = rs.getString(
							"professionalgroup_id");

                    r.startDate = rs.getDate("startdate").toLocalDate();
                    r.endDate = rs.getDate("enddate") != null ? rs.getDate(
                    		"enddate").toLocalDate() : null;
                    r.annualBaseSalary = rs.getDouble("annualbasesalary");
                    r.taxRate = rs.getDouble("taxrate");

                    r.settlement = rs.getDouble("settlement");
                    r.state = rs.getString("state");

                    contracts.add(r);
                }
	        }
	    } catch (SQLException e) {
	        throw new PersistenceException(e.getMessage());
	    }
	    return Optional.of(contracts);
	}
}

