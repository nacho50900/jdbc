package uo.ri.cws.application.service.mechanic.crud.commands;


public class ListMechanicByNif extends ListMechanic {
    
    public ListMechanicByNif(String mechanicNif) {
    	super("NIF", mechanicNif);
    }
}
