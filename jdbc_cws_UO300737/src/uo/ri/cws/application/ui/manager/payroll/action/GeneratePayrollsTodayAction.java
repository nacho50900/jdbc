package uo.ri.cws.application.ui.manager.payroll.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GeneratePayrollsTodayAction implements Action {

    @Override
    public void execute() throws BusinessException {

    	PayrollService mcs = Factories.service.forPayrollService();

		try {
			List<PayrollDto> payrolls = mcs.generateForPreviousMonth();
			
	        Console.println( payrolls.size() + " payrolls generated");
	        Printer.printPayrolls( payrolls );
	        Printer.printPayrolls( payrolls );
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
    }
}