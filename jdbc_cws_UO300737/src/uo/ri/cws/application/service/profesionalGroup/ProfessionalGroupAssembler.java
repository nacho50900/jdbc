package uo.ri.cws.application.service.profesionalgroup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway.ProfessionalGroupRecord;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;

public final class ProfessionalGroupAssembler {

    private ProfessionalGroupAssembler() { }

    public static ProfessionalGroupDto toDto(ProfessionalGroupRecord r) {
        if (r == null) {
			return null;
		}

        ProfessionalGroupDto dto = new ProfessionalGroupDto();
        dto.id                = r.id;             
        dto.version           = r.version;         
        dto.name              = r.name; 
        dto.trienniumPayment  = r.trienniumPayment; 
        dto.productivityRate  = r.productivityRate;
        return dto;
    }

    public static List<ProfessionalGroupDto> toDtoList(
    		Collection<ProfessionalGroupRecord> records) {
    	
        List<ProfessionalGroupDto> out = new ArrayList<>();
        if (records == null) {
			return out;
		}
        for (ProfessionalGroupRecord r : records) {
            out.add(toDto(r));
        }
        return out;
    }

    public static ProfessionalGroupRecord toRecord(ProfessionalGroupDto dto) {
        if (dto == null) {
			return null;
		}

        ProfessionalGroupRecord r = new ProfessionalGroupRecord(
                dto.name,
                dto.trienniumPayment,
                dto.productivityRate
        );
        r.version = dto.version;  
        r.id = (dto.id);      
        r.createdAt = LocalDateTime.now();
        r.updatedAt = LocalDateTime.now();
        return r;
    }

    public static List<ProfessionalGroupRecord> toRecordList(
    		Collection<ProfessionalGroupDto> dtos) {
        List<ProfessionalGroupRecord> out = new ArrayList<>();
        if (dtos == null) {
			return out;
		}
        for (ProfessionalGroupDto dto : dtos) {
            out.add(toRecord(dto));
        }
        return out;
    }

    /**
     * Copia los campos mutables del DTO al record existente.
     * No toca el id.
     */
    public static void updateRecord(
    		ProfessionalGroupRecord target, ProfessionalGroupDto source) {
    	
        if (target == null || source == null) {
			return;
		}
        target.name = (source.name);                      
        target.trienniumPayment = (source.trienniumPayment);  
        target.productivityRate = (source.productivityRate);   
        target.version = source.version;           
        target.id = source.id;
    }
}