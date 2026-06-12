package uo.ri.cws.application.ui.manager.contracts.contract.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindInForceContractsAction implements Action {

    @Override
    public void execute() throws BusinessException {

    	Console.println("\nInforce Contracts: \n");
    	
		ContractCrudService ccs = Factories.service.forContractCrudService();
	
		try {
			ccs.findInforceContracts().forEach(c -> Console.printf(
			"\t%s %s %s %s\n", c.id, c.mechanic.nif, c.settlement, c.state));
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
    }
}
