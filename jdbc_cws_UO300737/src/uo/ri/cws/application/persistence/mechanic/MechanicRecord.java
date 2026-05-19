package uo.ri.cws.application.persistence.mechanic;

import java.time.LocalDateTime;

public class MechanicRecord extends Mechanic {

    public LocalDateTime createdAt;
    public LocalDateTime updateAt;
    public String entityState;

    public MechanicRecord() {
	super();
	this.createdAt = LocalDateTime.now();
	this.updateAt = LocalDateTime.now();
	this.entityState = "ACTIVE";
    }
}