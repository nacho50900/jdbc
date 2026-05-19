package uo.ri.cws.application.persistence.invoice;

import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;

public class InvoiceAssembler {

    public static InvoiceDto toDto(InvoiceRecord record) {
        if (record == null) return null;

        InvoiceDto dto = new InvoiceDto();
        dto.id = record.id;
        dto.version = record.version;
        dto.amount = record.amount;
        dto.vat = record.vat;
        dto.number = record.number;
        dto.date = record.date;
        dto.state = record.state;

        return dto;
    }

    public static InvoiceRecord toRecord(InvoiceDto dto) {
        if (dto == null) return null;

        InvoiceRecord record = new InvoiceRecord();
        record.id = dto.id;
        record.version = dto.version;
        record.amount = dto.amount;
        record.vat = dto.vat;
        record.number = dto.number;
        record.date = dto.date;
        record.state = dto.state;

        return record;
    }

    public static List<InvoiceDto> toDtoList(List<InvoiceRecord> records) {
        if (records == null) return null;
        return records.stream()
                .map(InvoiceAssembler::toDto)
                .collect(Collectors.toList());
    }

    public static List<InvoiceRecord> toRecordList(List<InvoiceDto> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(InvoiceAssembler::toRecord)
                .collect(Collectors.toList());
    }
}

