package uo.ri.cws.application.persistence.contracttype;

import java.time.LocalDateTime;

public class ContractTypeRecord {

    public String id;
    public long version;

    public String name;
    public double compensationDays;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

}
