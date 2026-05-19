package uo.ri.cws.application.service.mechanic.crud.commands;


public class ListMechanicById extends ListMechanic {
    
    public ListMechanicById(String mechanicId) {
    	super("ID", mechanicId);
    }
}
