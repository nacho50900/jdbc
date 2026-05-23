package uo.ri.cws.application.service.mechanic.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class DeleteMechanic implements Command<MechanicDto> {

    private MechanicGateway mg = Factories.persistence.forMechanic();

    public DeleteMechanic(String mechanicId) {
	ArgumentChecks.isNotNull(mechanicId, "Mechanic id cannot be null");
	ArgumentChecks.isNotBlank(mechanicId, "Mechanic id cannot be blank");
	this.mechanicId = mechanicId;
    }

    private String mechanicId;

    // private static final String TMECHANICS_DELETE = "DELETE FROM TMECHANICS"
    // + "WHERE ID = ?";
    // All sql moved to queries.properties file

    private MechanicDto dto;

    @Override
    public MechanicDto execute() throws BusinessException {
		Optional<MechanicRecord> om = mg.findById(mechanicId);
		BusinessChecks.exists(om, "The mechanic does not exist");

		checkNoWorkorders();
		checkNoInterventions();
		checkNoContract();
		checkNoActiveContract(); //Ampliation: not need it
		
		mg.remove(mechanicId);
		return dto;
    }

	private void checkNoWorkorders() throws BusinessException {
		try {
	        Connection c = Jdbc.getCurrentConnection();
	        if (c == null || c.isClosed()) { //Just in case...
	            c = Jdbc.createThreadConnection(); 
	        }
		    try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence("TMECHANICS_HAS_WORKORDERS"))) {
		    	pst.setString(1, mechanicId);
				try (ResultSet rs = pst.executeQuery()) {
					long counter = 0;
					if (rs.next()) {
						counter = rs.getLong(1);
					}
					if (counter > 0) {
						throw new BusinessException("Can not delete a mechanic "
								+ "with workOrders assigned");
					}
				}
		    }
	
		} catch (SQLException e) {
		    //throw new RuntimeException(e);
		}
	}
	
	private void checkNoInterventions() throws BusinessException {
		try {
	        Connection c = Jdbc.getCurrentConnection();
	        if (c == null || c.isClosed()) { //Just in case...
	            c = Jdbc.createThreadConnection(); 
	        }
		    try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence("TMECHANICS_HAS_INTERVENTIONS"))) {
		    	pst.setString(1, mechanicId);
				try (ResultSet rs = pst.executeQuery()) {
					long counter = 0;
					if (rs.next()) {
						counter = rs.getLong(1);
					}
					if (counter > 0) {
						throw new BusinessException("Can not delete a mechanic "
								+ "with interventions assigned");
					}
				}
		    }
	
		} catch (SQLException e) {
		    throw new RuntimeException(e); 
		}
	}
	
	private void checkNoContract() throws BusinessException {
		try {
	        Connection c = Jdbc.getCurrentConnection();
	        if (c == null || c.isClosed()) { //Just in case...
	            c = Jdbc.createThreadConnection(); 
	        }
		    try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence("TMECHANICS_HAS_CONTRACT"))) {
		    	pst.setString(1, mechanicId);
		    	try (ResultSet rs = pst.executeQuery()) {
		    		long counter = 0;
					if (rs.next()) {
						counter = rs.getLong(1);
					}
					if (counter > 0) {
						throw new BusinessException("Can not delete a mechanic "
								+ "with conctracts assigned");
					}
		    	}
		    }
	
		} catch (SQLException e) {
		    throw new RuntimeException(e); 
		}
	}
	
	private void checkNoActiveContract() throws BusinessException {
		try {
	        Connection c = Jdbc.getCurrentConnection();
	        if (c == null || c.isClosed()) { //Just in case...
	            c = Jdbc.createThreadConnection(); 
	        }
		    try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence("TMECHANICS_HAS_ACTIVE_CONTRACT"))) {
		    	pst.setString(1, mechanicId);
				try(ResultSet rs = pst.executeQuery()) {
					long counter = 0;
					if (rs.next()) {
						counter = rs.getLong(1);
					}
					if (counter > 0) {
						throw new BusinessException("Can not delete a mechanic "
								+ "with interventions assigned");
					}
				}
		    }
		} catch (SQLException e) {
		    throw new RuntimeException(e); 
		}
	}
}
