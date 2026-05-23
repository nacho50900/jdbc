package uo.ri.cws.application.persistence.contracttype;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;

public final class ContractTypeAssembler {

    private ContractTypeAssembler() { }

    public static ContractTypeDto toDto(ContractTypeRecord r) {
        if (r == null) {
            return null;
        }
        ContractTypeDto dto = new ContractTypeDto();
        dto.id = r.id;
        dto.version = r.version;
        dto.name = r.name;
        dto.compensationDays = r.compensationDays;
        return dto;
    }

    public static ContractTypeRecord toRecord(ContractTypeDto dto) {
        if (dto == null) {
            return null;
        }
        ContractTypeRecord r = new ContractTypeRecord();
        r.id = dto.id;
        r.version = dto.version;
        r.name = dto.name;
        r.compensationDays = dto.compensationDays;
        r.createdAt = LocalDateTime.now();
        r.updatedAt = LocalDateTime.now();
        return r;
    }

    public static List<ContractTypeDto> toDtoList(
            List<ContractTypeRecord> records) {
        List<ContractTypeDto> out = new ArrayList<>();
        if (records == null) {
            return out;
        }
        for (ContractTypeRecord r : records) {
            out.add(toDto(r));
        }
        return out;
    }

}
