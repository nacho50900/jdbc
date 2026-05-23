package uo.ri.cws.application.service.contract.crud.commands;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractAssembler;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddContract implements Command<ContractDto> {

    private ContractDto dto;
    private ContractGateway cg = Factories.persistence.forContract();

    public AddContract(ContractDto dto) {
		ArgumentChecks.isNotNull(dto, "Contract cannot be null");
		this.dto = dto;
    }

	@Override
	public ContractDto execute() throws BusinessException {
		Optional<ContractRecord> oc = cg.findById(dto.id);
		BusinessChecks.doesNotExist(oc,"The contract already exists");
		
		// Rellena los campos generados en la capa de aplicación
	    if (dto.id == null || dto.id.isBlank()) {
	        dto.id = java.util.UUID.randomUUID().toString();
	    }
	    if (dto.version == 0) {
	        dto.version = 1L;
	    }
	    
		cg.add(ContractAssembler.toRecord(dto));
		return dto;
	}
}
