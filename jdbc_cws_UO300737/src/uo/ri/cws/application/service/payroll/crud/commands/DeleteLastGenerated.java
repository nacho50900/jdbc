package uo.ri.cws.application.service.payroll.crud.commands;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.util.exception.BusinessException;

public class DeleteLastGenerated  implements Command<Integer> {

	private PayrollGateway pg = Factories.persistence.forPayroll(); 
	
	@Override
	public Integer execute() throws BusinessException {
		return pg.deleteLastGenerated();
	}


}
