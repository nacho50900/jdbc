package uo.ri.cws.application.persistence.mechanic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;
import uo.ri.cws.application.persistence.PersistenceException;

public interface MechanicGateway extends Gateway<uo.ri.cws.application.
persistence.mechanic.MechanicGateway.MechanicRecord> {

    Optional<MechanicRecord> findByNif(String nif) throws PersistenceException;

    List<MechanicRecord> findMechanicsWithValidContract()
            throws PersistenceException;

    boolean hasWorkOrders(String mechanicId) throws PersistenceException;

    boolean hasInterventions(String mechanicId) throws PersistenceException;

    boolean hasContracts(String mechanicId) throws PersistenceException;
    
    public class MechanicRecord {

        public String id;
        public long version;

        public String nif;
        public String name;
        public String surname;
        
        public LocalDateTime createdAt;
        public LocalDateTime updateAt;
        public String entityState;

        public MechanicRecord() {
	    	this.createdAt = LocalDateTime.now();
	    	this.updateAt = LocalDateTime.now();
	    	this.entityState = "ACTIVE";
        }
    }	
}