package uo.ri.cws.application.persistence.professionalgroup;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProfessionalGroupRecord {
	
	public String id;
	public long version;
	
	public String name;
	public double trienniumPayment;
	public double productivityRate;
	
	public LocalDateTime createdAt;
	public LocalDateTime updateAt;
	
	public ProfessionalGroupRecord(
			String groupName, double trienniumSalary, double productivityRate) {
		
		this.name = groupName;
		this.trienniumPayment = trienniumSalary;
		this.productivityRate = productivityRate;
		this.id = UUID.randomUUID().toString(); //Done here?
		this.createdAt = LocalDateTime.now();
		this.updateAt = LocalDateTime.now();
	}

}
