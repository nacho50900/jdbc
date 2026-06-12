package uo.ri.cws.application.ui.manager.payroll.action;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class ListPayrollsOfMechanicAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
        String mechanicId = Console.readString("Mechanic id");

    	PayrollService mcs = Factories.service.forPayrollService();
    	List<PayrollSummaryDto> payrolls = new ArrayList<PayrollSummaryDto>();
    	
		try {
	    	payrolls = mcs.findSummarizedByMechanicId(mechanicId);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
    	
        if (payrolls.isEmpty()) {
            Console.println("No payrolls found for this mechanic");
            return;
        }
        
        for (PayrollSummaryDto dto : payrolls) {
            Printer.printPayrollSummary(dto);
        }
    }
}