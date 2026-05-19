package uo.ri.cws.application.persistence.mechanic;

import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.Gateway;

public interface MechanicGateway extends Gateway<MechanicRecord> {

    /**
     * 
     * @param nif
     * @return
     */
    public Optional<MechanicRecord> findByNif(String nif);

    //MechanicExtension
	public List<MechanicRecord> findMechanicsWithValidContract();

	//public boolean hasWorkOrders(String mechanicId); NOT modify interfaces

}
