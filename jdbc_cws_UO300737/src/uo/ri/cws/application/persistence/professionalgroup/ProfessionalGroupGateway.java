package uo.ri.cws.application.persistence.professionalgroup;

import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface ProfessionalGroupGateway extends
		Gateway<ProfessionalGroupRecord> {
	
    /**
     * 
     * @param nif
     * @return
     */
    public Optional<ProfessionalGroupRecord> findByName(String name);

}
