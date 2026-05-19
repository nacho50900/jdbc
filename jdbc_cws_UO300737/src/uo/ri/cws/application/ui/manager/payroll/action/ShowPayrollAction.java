package uo.ri.cws.application.ui.manager.payroll.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ShowPayrollAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
        String payrollId = Console.readString("Payroll id");

    	PayrollService mcs = Factories.service.forPayrollService();
    	Optional<PayrollDto> op = mcs.findById(payrollId);
    	
    	if (op.isEmpty()) {
    		 Console.println("No payrolls found for this professional group");
             return;
    	}
        Printer.printPayrollDetails(op.get());
    }
}