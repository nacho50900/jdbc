package uo.ri.cws.application.ui.manager.payroll.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class DeleteLastMonthPayrollAction implements Action {

	
    @Override
    public void execute() throws BusinessException {

    	PayrollService mcs = Factories.service.forPayrollService();

		try {
	    	mcs.deleteLastGenerated();
	        Console.println("Last month's payrolls deleted");
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}

    }
}