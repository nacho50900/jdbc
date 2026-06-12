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

public class ListPayrollsOfProfGroupAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
        String groupName = Console.readString("Professional group name");

    	PayrollService mcs = Factories.service.forPayrollService();
    	List<PayrollSummaryDto> payrolls = new ArrayList<PayrollSummaryDto>();
    	
		try {
	    	payrolls = mcs.findSummarizedByProfessionalGroupName(groupName);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
		
        if (payrolls.isEmpty()) {
            Console.println("No payrolls found for this professional group");
            return;
        }
        
        for (PayrollSummaryDto dto : payrolls) {
            Printer.printPayrollSummary(dto);
        }
    }
}