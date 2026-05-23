package uo.ri.cws.application.service.mechanic.crud.commands;


public class FindMechanicById extends FindMechanic {
    
    public FindMechanicById(String mechanicId) {
    	super("ID", mechanicId);
    }
}
