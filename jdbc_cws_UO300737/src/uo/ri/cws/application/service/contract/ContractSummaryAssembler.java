package uo.ri.cws.application.service.contract;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.contract.ContractGateway.ContractSummaryRecord;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

/**
 * Converts between ContractSummaryRecord (persistence) and ContractSummaryDto (service).
 * No business logic. No persistence access.
 */
public class ContractSummaryAssembler {

    public static ContractSummaryRecord toRecord(ContractSummaryDto dto) {
        ContractSummaryRecord r = new ContractSummaryRecord();
        r.id          = dto.id;
        r.nif         = dto.nif;
        r.settlement  = dto.settlement;
        r.state       = dto.state;
        r.numPayrolls = dto.numPayrolls;
        return r;
    }

    public static ContractSummaryDto toDto(ContractSummaryRecord r) {
        ContractSummaryDto dto = new ContractSummaryDto();
        dto.id          = r.id;
        dto.nif         = r.nif;
        dto.settlement  = r.settlement;
        dto.state       = r.state;
        dto.numPayrolls = r.numPayrolls;
        return dto;
    }

    public static List<ContractSummaryRecord> toRecordList(
            List<ContractSummaryDto> dtos) {
        List<ContractSummaryRecord> records = new ArrayList<>();
        if (dtos == null) {
            return records;
        }
        for (ContractSummaryDto dto : dtos) {
            records.add(toRecord(dto));
        }
        return records;
    }

    public static List<ContractSummaryDto> toDtoList(
            List<ContractSummaryRecord> records) {
        List<ContractSummaryDto> dtos = new ArrayList<>();
        if (records == null) {
            return dtos;
        }
        for (ContractSummaryRecord r : records) {
            dtos.add(toDto(r));
        }
        return dtos;
    }
}