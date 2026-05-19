package uo.ri.cws.application.persistence.mechanic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;

public class MechanicAssembler {

    public static Optional<MechanicRecord> toRecord(ResultSet rs)
	throws SQLException {
	if (rs.next()) {
	    // MechanicRecordAssembler could be developed
	    // And with payroll,contract...
	    MechanicRecord m = new MechanicRecord();
	    m.id = rs.getString(1);
	    m.name = rs.getString(2);
	    m.surname = rs.getString(3);
	    m.nif = rs.getString(4);
	    m.version = rs.getLong(5);
	    m.createdAt = LocalDateTime.now();
	    m.updateAt = LocalDateTime.now();
	    m.entityState = "ACTIVE";
	    return Optional.of(m);
	}
	return Optional.empty();
    }

    public static MechanicDto toDto(MechanicRecord record) {
	MechanicDto dto = new MechanicDto();
	dto.id = record.id;
	dto.version = record.version;

	dto.nif = record.nif;
	dto.name = record.name;
	dto.surname = record.surname;
	record.createdAt = LocalDateTime.now();
	record.updateAt = LocalDateTime.now();
	record.entityState = "ACTIVE";
	return dto;
    }

    public static MechanicRecord toRecord(MechanicDto dto) {
	MechanicRecord record = new MechanicRecord();
	record.id = dto.id;
	record.version = dto.version;

	record.nif = dto.nif;
	record.name = dto.name;
	record.surname = dto.surname;
	
	return record;
    }

    public static List<MechanicDto> toDtoList(List<MechanicRecord> records) {
	return records.stream()
		      .map(MechanicAssembler::toDto)
		      .collect(Collectors.toList());
    }

    public static List<MechanicRecord> toRecordList(List<MechanicDto> dtos) {
	return dtos.stream()
		   .map(MechanicAssembler::toRecord)
		   .collect(Collectors.toList());
    }

}
