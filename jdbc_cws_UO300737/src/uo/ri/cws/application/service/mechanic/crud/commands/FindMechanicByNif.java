package uo.ri.cws.application.service.mechanic.crud.commands;


public class FindMechanicByNif extends FindMechanic {
    
    public FindMechanicByNif(String mechanicNif) {
    	super("NIF", mechanicNif);
    }
}
