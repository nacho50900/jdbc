package uo.ri.cws.application.persistence.contract;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;

public class ContractAssembler {

    // --- Individual conversion ---

    public static ContractRecord toRecord(ContractDto dto) {
        ContractRecord record = new ContractRecord();
        record.id = dto.id;
        record.version = dto.version;

        record.mechanicId = dto.mechanic.id;
        record.contractTypeId = dto.contractType.id;
        record.professionalGroupId = dto.professionalGroup.id;

        record.startDate = dto.startDate;
        record.endDate = dto.endDate;
        record.annualBaseSalary = dto.annualBaseSalary;
        record.taxRate = dto.taxRate;

        record.settlement = dto.settlement;
        record.state = dto.state;

        return record;
    }

    public static ContractDto toDto(ContractRecord record) {
        ContractDto dto = new ContractDto();
        dto.id = record.id;
        dto.version = record.version;

        dto.mechanic.id = record.mechanicId;
        dto.contractType.id = record.contractTypeId;
        dto.professionalGroup.id = record.professionalGroupId;

        dto.startDate = record.startDate;
        dto.endDate = record.endDate;
        dto.annualBaseSalary = record.annualBaseSalary;
        dto.taxRate = record.taxRate;

        dto.settlement = record.settlement;
        dto.state = record.state;

        return dto;
    }

    // --- List conversion ---

    public static List<ContractRecord> toRecordList(List<ContractDto> dtos) {
        List<ContractRecord> records = new ArrayList<>();
        if (dtos == null) {
			return records;
		}

        for (ContractDto dto : dtos) {
            records.add(toRecord(dto));
        }
        return records;
    }

    public static List<ContractDto> toDtoList(List<ContractRecord> records) {
        List<ContractDto> dtos = new ArrayList<>();
        if (records == null) {
			return dtos;
		}

        for (ContractRecord record : records) {
            dtos.add(toDto(record));
        }
        return dtos;
    }
}