package uo.ri.cws.application.persistence.contract.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import uo.ri.cws.application.persistence.PersistenceException;
import uo.ri.cws.application.persistence.contract.ContractAssembler;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractSummaryAssembler;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class ContractGatewayImpl implements ContractGateway{

	@Override
	public void add(ContractRecord t) throws PersistenceException {
		// TODO Auto-generated method stub
		
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
                    ContractDto dto = new ContractDto();
                    dto.id = UUID.randomUUID().toString();
                    dto.version = 1L;
                    
                    dto.mechanic.id = rs.getString("mechanic_id");
					dto.contractType.id = rs.getString("contracttype_id");
					dto.professionalGroup.id = rs.getString(
							"professionalgroup_id");

                    dto.startDate = rs.getDate("startdate").toLocalDate();
                    dto.endDate = rs.getDate("enddate") != null ? rs.getDate(
                    		"enddate").toLocalDate() : null;
                    dto.annualBaseSalary = rs.getDouble("annualbasesalary");
                    dto.taxRate = rs.getDouble("taxrate");

                    dto.settlement = rs.getDouble("settlement");
                    dto.state = rs.getString("state");

                    contracts.add(ContractAssembler.toRecord(dto));
                }
	        }
	    } catch (SQLException e) {
	        throw new BusinessException(e.getMessage());
	    }
	    return Optional.of(contracts);
	}
}

