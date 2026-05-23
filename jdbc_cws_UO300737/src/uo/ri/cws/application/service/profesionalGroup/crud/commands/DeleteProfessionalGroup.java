package uo.ri.cws.application.service.profesionalGroup.crud.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupAssembler;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.jdbc.Jdbc;
import uo.ri.util.jdbc.Queries;

public class DeleteProfessionalGroup implements Command<ProfessionalGroupDto> {

	
    private String groupName;
    private ProfessionalGroupGateway mp = 
    		Factories.persistence.forProfessionalGroup();

    public DeleteProfessionalGroup(String groupName) {
    	ArgumentChecks.isNotNull(groupName,
    			"ProfessionalGroupDto cannot be null");
    	ArgumentChecks.isNotBlank(groupName,
    			"groupName cannot be blank");
    	this.groupName = groupName;
    }
    
	@Override
	public ProfessionalGroupDto execute() throws BusinessException {
		Optional<ProfessionalGroupRecord> om = mp.findByName(groupName);
		BusinessChecks.exists(om, "Can not remove a group that does not exist");
		
		//Checks
		String groupId = om.get().id;
		checkNoContract(groupId);
		
		mp.remove(groupId);
		return ProfessionalGroupAssembler.toDto(om.get());
	}
	
	private void checkNoContract(String id) throws BusinessException {
		try {
	        Connection c = Jdbc.getCurrentConnection();
//	        if (c == null || c.isClosed()) { //Just in case...
//	            c = Jdbc.createThreadConnection(); 
//	        }
		    try (PreparedStatement pst = c.prepareStatement(
			Queries.getSQLSentence(
					"TCONTRACTS_COUNT_BY_PROFESSIONALGROUP_ID"))) {
		    	pst.setString(1, id);
		    	System.out.println(id);
		    	try (ResultSet rs = pst.executeQuery()) {
		    		long counter = 0;
					if (rs.next()) {
						counter = rs.getLong(1);
					}
					if (counter > 0) {
						throw new BusinessException(
								"Can not delete a professional Group "
								+ "with conctracts assigned");
					}
		    	}
		    }
	
		} catch (SQLException e) {
		    //throw new RuntimeException(e); 
		}
	}

}
