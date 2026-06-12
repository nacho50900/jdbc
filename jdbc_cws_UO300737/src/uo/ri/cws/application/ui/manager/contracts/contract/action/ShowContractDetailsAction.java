package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ShowContractDetailsAction implements Action {

    @Override
    public void execute() throws BusinessException {
        ContractCrudService service =
                Factories.service.forContractCrudService();

        List<ContractSummaryDto> all = new ArrayList<ContractSummaryDto>();
		try {
			all = service.findAll();
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
		
        if (all.isEmpty()) {
            Console.println("No contracts found");
            return;
        }
        for (ContractSummaryDto c : all) {
            Printer.printContractSummary(c);
        }

        String id = Console.readString("Contract id to show");
        
        Optional<ContractDto> result = Optional.empty();
        		
		try {
			result = service.findById(id);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}

        if (result.isEmpty()) {
            Console.println("Contract not found");
            return;
        }
        Printer.printContractDetails(result.get());
    }

}
