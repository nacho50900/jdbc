package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteContractAction implements Action {

    @Override
    public void execute() throws BusinessException {
        ContractCrudService service =
                Factories.service.forContractCrudService();

        List<ContractSummaryDto> all = service.findAll();
        if (all.isEmpty()) {
            Console.println("No contracts found");
            return;
        }
        for (ContractSummaryDto c : all) {
            Printer.printContractSummary(c);
        }

        String id = Console.readString("Contract id to delete");
        
		try {
	        service.delete(id);
	        Console.println("Contract deleted");
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
    }

}
