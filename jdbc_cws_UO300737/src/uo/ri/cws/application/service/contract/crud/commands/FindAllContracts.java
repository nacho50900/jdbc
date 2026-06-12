package uo.ri.cws.application.service.contract.crud.commands;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.contract.ContractGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway.MechanicRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.contract.ContractAssembler;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;

public class FindAllContracts implements Command<List<ContractSummaryDto>> {

    private ContractGateway cg = Factories.persistence.forContract();
    private MechanicGateway mg = Factories.persistence.forMechanic();

    @Override
    public List<ContractSummaryDto> execute() {
    	List<ContractDto> contracts = ContractAssembler.toDtoList(cg.findAll());
    	assignMechanics(contracts);
    	return ContractAssembler.toSummaryDtoList(contracts);	
    }

    /**
     * As from the persistence layer (findAll()) we only have access to the record
     * the unique value we can save from a mechanic is the id so we must 
     * reconstruct it in the service by it.
     */
	private void assignMechanics(List<ContractDto> contracts) {
		for (ContractDto contract : contracts) {
			Optional<MechanicRecord> om = mg.findById(contract.mechanic.id);
			if (om.isPresent()) {
				contract.mechanic.name = om.get().name;
				contract.mechanic.nif = om.get().nif;
				contract.mechanic.surname = om.get().surname;
				
			}
		}
	}
}
