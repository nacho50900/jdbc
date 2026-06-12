package uo.ri.cws.application.service.invoice;

import java.util.ArrayList;
import java.util.List;

import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoiceRecord;
import uo.ri.cws.application.persistence.invoice.InvoiceGateway.InvoicingWorkOrderRecord;
import uo.ri.cws.application.persistence.workorder.WorkOrderGateway.WorkorderRecord;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;

/**
 * Converts between InvoiceRecord (persistence) and InvoiceDto (service).
 * Assemblers must not contain business logic or persistence access.
 */
public class InvoiceAssembler {

    public static InvoiceDto toDto(InvoiceRecord record) {
        if (record == null) {
            return null;
        }
        InvoiceDto dto = new InvoiceDto();
        dto.id      = record.id;
        dto.version = record.version;
        dto.amount  = record.amount;
        dto.vat     = record.vat;
        dto.number  = record.number;
        dto.date    = record.date;
        dto.state   = record.state;
        return dto;
    }

    public static InvoiceRecord toRecord(InvoiceDto dto) {
        if (dto == null) {
            return null;
        }
        InvoiceRecord record = new InvoiceRecord();
        record.id      = dto.id;
        record.version = dto.version;
        record.amount  = dto.amount;
        record.vat     = dto.vat;
        record.number  = dto.number;
        record.date    = dto.date;
        record.state   = dto.state;
        return record;
    }

    public static List<InvoiceDto> toDtoList(List<InvoiceRecord> records) {
        List<InvoiceDto> out = new ArrayList<>();
        if (records == null) {
            return out;
        }
        for (InvoiceRecord r : records) {
            out.add(toDto(r));
        }
        return out;
    }
    
    public static InvoicingWorkOrderDto toInvoicingDto(InvoicingWorkOrderRecord r) {
        InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
        dto.id          = r.id;
        dto.description = r.description;
        dto.date        = r.date;
        dto.state       = r.state;
        dto.amount      = r.amount;
        return dto;
    }

    public static List<InvoicingWorkOrderDto> toInvoicingDtoList(
    		List<WorkorderRecord> list) {
        List<InvoicingWorkOrderDto> dtos = new ArrayList<>();
        if (list == null) {
            return dtos;
        }
        for (WorkorderRecord r : list) {
        	InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();
            dto.id          = r.id;
            dto.description = r.description;
            dto.date        = r.date;
            dto.state       = r.state;
            dto.amount      = r.amount;

        	dtos.add(dto);
        }
        return dtos;
    }
}