package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListContractsOfMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String nif = Console.readString("Mechanic NIF");

        ContractCrudService service =
                Factories.service.forContractCrudService();
        
        List<ContractSummaryDto> contracts = new ArrayList<ContractSummaryDto>();

		try {
			contracts = service.findByMechanicNif(nif);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
		
        if (contracts.isEmpty()) {
            Console.println("No contracts found for this mechanic");
            return;
        }
        for (ContractSummaryDto c : contracts) {
            Printer.printContractSummary(c);
        }
    }

}
