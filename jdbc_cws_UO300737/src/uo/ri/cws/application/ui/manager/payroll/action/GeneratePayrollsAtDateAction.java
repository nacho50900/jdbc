package uo.ri.cws.application.ui.manager.payroll.action;

import java.time.LocalDate;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class GeneratePayrollsAtDateAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
        LocalDate date = Console.readDate("Date (yyyy-MM-dd)");

    	PayrollService mcs = Factories.service.forPayrollService();
    	List<PayrollDto> payrolls = mcs.generateForPreviousMonthOf(date);

    	// Print result
        Console.println(payrolls.size() + " payrolls generated for the specified date");
        Printer.printPayrolls( payrolls );
    }
}