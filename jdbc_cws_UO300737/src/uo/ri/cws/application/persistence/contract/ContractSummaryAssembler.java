package uo.ri.cws.application.persistence.contract;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.contract.ContractGateway.ContractSummaryRecord;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

public class ContractSummaryAssembler {

    public static ContractSummaryRecord toRecord(ContractSummaryDto dto) {
        ContractSummaryRecord record = new ContractSummaryRecord();
        record.id = dto.id;
        record.nif = dto.nif;
        record.settlement = dto.settlement;
        record.state = dto.state;
        record.numPayrolls = dto.numPayrolls;
        return record;
    }

    public static ContractSummaryDto toDto(ContractSummaryRecord record) {
        ContractSummaryDto dto = new ContractSummaryDto();
        dto.id = record.id;
        dto.nif = record.nif;
        dto.settlement = record.settlement;
        dto.state = record.state;
        dto.numPayrolls = record.numPayrolls;
        return dto;
    }

    public static List<ContractSummaryRecord> toRecordList(
    		List<ContractSummaryDto> dtos) {
    	
        List<ContractSummaryRecord> records = new ArrayList<>();
        if (dtos == null)
		 {
			return records; // devuelve lista vacía si records es null
		}
        for (ContractSummaryDto dto : dtos) {
            records.add(toRecord(dto));
        }
        return records;
    }


	public static List<ContractSummaryDto> toDtoList(
			List<ContractSummaryRecord> records) {
		
	    List<ContractSummaryDto> dtos = new ArrayList<>();
	    if (records == null)
		 {
			return dtos; // devuelve lista vacía si records es null
		}
	    for (ContractSummaryRecord record : records) {
	        dtos.add(toDto(record));
	    }
	    return dtos;
	}

}