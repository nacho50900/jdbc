package uo.ri.cws.application.persistence.mechanic;

public abstract class Mechanic {
    public String id;
    public long version;

    public String nif;
    public String name;
    public String surname;

    public Mechanic() {
	// this.id = UUID.randomUUID().toString();
	// this.version = 1L;
    // here, it is already in the Action
    }
}
