package uo.ri.cws.application.service.contract.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.contract.ContractGateway.ContractSummaryRecord;
import uo.ri.cws.application.persistence.contract.ContractSummaryAssembler;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessException;

public class ListMechanicContractsByNif implements 
	Command<List<ContractSummaryDto>>{

	private String nif;
    private ContractGateway cg = Factories.persistence.forContract();

    public ListMechanicContractsByNif(String nif) {
		ArgumentChecks.isNotNull(nif, "Mechanic nif cannot be null");
		ArgumentChecks.isNotBlank(nif, "Mechanic nif or nif cannot be blank");
		this.nif = nif;
    }

    @Override
	public List<ContractSummaryDto> execute() throws BusinessException {
	Optional<List<ContractSummaryRecord>> om = cg.findByMechanicNif(nif);
	
	if (om.isEmpty()) {
		return new ArrayList<ContractSummaryDto>();
	}
	return ContractSummaryAssembler.toDtoList(om.get());
    }
}
