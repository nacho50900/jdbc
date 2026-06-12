package uo.ri.cws.application.service.payroll;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollSummaryRecord;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;

public class PayrollAssembler {

    public static PayrollRecord toRecord(PayrollDto dto) {
        PayrollRecord record = new PayrollRecord();
        record.id = dto.id;
        record.version = dto.version;

        record.contractId = dto.contractId;
        record.date = dto.date;

        record.baseSalary = dto.baseSalary;
        record.extraSalary = dto.extraSalary;
        record.productivityEarning = dto.productivityEarning;
        record.trienniumEarning = dto.trienniumEarning;

        record.taxDeduction = dto.taxDeduction;
        record.nicDeduction = dto.nicDeduction;

        record.netSalary = dto.netSalary;
        record.grossSalary = dto.grossSalary;
        record.totalDeductions = dto.totalDeductions;

        return record;
    }

    public static PayrollDto toDto(PayrollRecord record) {
        PayrollDto dto = new PayrollDto();
        dto.id = record.id;
        dto.version = record.version;

        dto.contractId = record.contractId;
        dto.date = record.date;

        dto.baseSalary = record.baseSalary;
        dto.extraSalary = record.extraSalary;
        dto.productivityEarning = record.productivityEarning;
        dto.trienniumEarning = record.trienniumEarning;

        dto.taxDeduction = record.taxDeduction;
        dto.nicDeduction = record.nicDeduction;

        dto.netSalary = record.netSalary;
        dto.grossSalary = record.grossSalary;
        dto.totalDeductions = record.totalDeductions;

        return dto;
    }

    public static List<PayrollRecord> toRecordList(List<PayrollDto> dtos) {
        List<PayrollRecord> records = new ArrayList<>();
        if (dtos == null) {
			return records;
		}

        for (PayrollDto dto : dtos) {
            records.add(toRecord(dto));
        }
        return records;
    }

    public static List<PayrollDto> toDtoList(List<PayrollRecord> records) {
        List<PayrollDto> dtos = new ArrayList<>();
        if (records == null) {
			return dtos;
		}

        for (PayrollRecord record : records) {
            dtos.add(toDto(record));
        }
        return dtos;
    }

    // --- PayrollSummaryDto <-> PayrollSummaryRecord ---

    public static PayrollSummaryRecord toSummaryRecord(PayrollSummaryDto dto) {
        PayrollSummaryRecord record = new PayrollSummaryRecord();
        record.id = dto.id;
        record.date = dto.date;
        record.netSalary = dto.netSalary;
        return record;
    }

    public static PayrollSummaryDto toSummaryDto(PayrollSummaryRecord record) {
        PayrollSummaryDto dto = new PayrollSummaryDto();
        dto.id = record.id;
        dto.date = record.date;
        dto.netSalary = record.netSalary;
        return dto;
    }

    public static List<PayrollSummaryRecord> toSummaryRecordList(
    		List<PayrollSummaryDto> dtos) {
        List<PayrollSummaryRecord> records = new ArrayList<>();
        if (dtos == null) {
			return records;
		}

        for (PayrollSummaryDto dto : dtos) {
            records.add(toSummaryRecord(dto));
        }
        return records;
    }

    public static List<PayrollSummaryDto> toSummaryDtoList(
    		List<PayrollSummaryRecord> records) {
        List<PayrollSummaryDto> dtos = new ArrayList<>();
        if (records == null) {
			return dtos;
		}

        for (PayrollSummaryRecord record : records) {
            dtos.add(toSummaryDto(record));
        }
        return dtos;
    }
    
    
    // Normal -> Summary
    
    public static PayrollSummaryRecord toSummaryRecord(PayrollRecord record) {
        PayrollSummaryRecord summary = new PayrollSummaryRecord();
        summary.id = record.id;
        summary.date = record.date;
        summary.netSalary = record.netSalary;
        summary.createdAt = record.createdAt;
        summary.updatedAt = record.updatedAt;
        return summary;
    }
    
    public static PayrollSummaryDto toSummaryDto(PayrollDto dto) {
        PayrollSummaryDto summary = new PayrollSummaryDto();
        summary.id = dto.id;
        summary.date = dto.date;
        summary.netSalary = dto.netSalary;
        return summary;
    }
    
    public static List<PayrollSummaryRecord> toSummaryRecordList2(
    		List<PayrollRecord> records) {
        List<PayrollSummaryRecord> summaries = new ArrayList<>();
        if (records == null) {
            return summaries;
        }

        for (PayrollRecord record : records) {
            summaries.add(toSummaryRecord(record));
        }
        return summaries;
    }
    public static List<PayrollSummaryDto> toSummaryDtoList2(
    		List<PayrollDto> dtos) {
        List<PayrollSummaryDto> summaries = new ArrayList<>();
        if (dtos == null) {
            return summaries;
        }

        for (PayrollDto dto : dtos) {
            summaries.add(toSummaryDto(dto));
        }
        return summaries;
    }

}
