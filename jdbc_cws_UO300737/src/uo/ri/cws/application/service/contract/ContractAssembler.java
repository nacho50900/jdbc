package uo.ri.cws.application.service.contract;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractSummaryRecord;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

/**
 * Converts between ContractRecord (persistence) and ContractDto (service).
 * No business logic. No persistence access.
 */
public class ContractAssembler {

    public static ContractRecord toRecord(ContractDto dto) {
        ContractRecord r = new ContractRecord();
        r.id                  = dto.id;
        r.version             = dto.version;
        r.mechanicId          = dto.mechanic.id;
        r.contractTypeId      = dto.contractType.id;
        r.professionalGroupId = dto.professionalGroup.id;
        r.startDate           = dto.startDate;
        r.endDate             = dto.endDate;
        r.annualBaseSalary    = dto.annualBaseSalary;
        r.taxRate             = dto.taxRate;
        r.settlement          = dto.settlement;
        r.state               = dto.state;
        return r;
    }

    public static ContractDto toDto(ContractRecord r) {
        ContractDto dto = new ContractDto();
        dto.id                    = r.id;
        dto.version               = r.version;
        dto.mechanic.id           = r.mechanicId;
        dto.contractType.id       = r.contractTypeId;
        dto.professionalGroup.id  = r.professionalGroupId;
        dto.startDate             = r.startDate;
        dto.endDate               = r.endDate;
        dto.annualBaseSalary      = r.annualBaseSalary;
        dto.taxRate               = r.taxRate;
        dto.settlement            = r.settlement;
        dto.state                 = r.state;
        return dto;
    }

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
        for (ContractRecord r : records) {
            dtos.add(toDto(r));
        }
        return dtos;
    }

    public static ContractSummaryDto toSummaryDto(ContractSummaryRecord r) {
        ContractSummaryDto dto = new ContractSummaryDto();
        dto.id          = r.id;
        dto.nif         = r.nif;
        dto.settlement  = r.settlement;
        dto.state       = r.state;
        dto.numPayrolls = r.numPayrolls;
        return dto;
    }

    public static List<ContractSummaryDto> toSummaryDtoListFromRecords(
            List<ContractSummaryRecord> records) {
        List<ContractSummaryDto> dtos = new ArrayList<>();
        if (records == null) {
            return dtos;
        }
        for (ContractSummaryRecord r : records) {
            dtos.add(toSummaryDto(r));
        }
        return dtos;
    }
    
    public static List<ContractSummaryDto> toSummaryDtoList(List<ContractDto> dtos) {
        List<ContractSummaryDto> summaries = new ArrayList<>();
        if (dtos == null) {
            return summaries;
        }
        for (ContractDto dto : dtos) {
            ContractSummaryDto sdto = new ContractSummaryDto();
            sdto.id          = dto.id;
            sdto.nif         = dto.mechanic.nif;
            sdto.settlement  = dto.settlement;
            sdto.state       = dto.state;
            sdto.numPayrolls = 0;
            summaries.add(sdto);
        }
        return summaries;
    }
   
}