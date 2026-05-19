package uo.ri.cws.application.service.payroll.crud.commands;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollAssembler;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollDto;
import uo.ri.util.exception.BusinessException;

public class GenerateForPreviousMonth implements Command<List<PayrollDto>> {

    private PayrollGateway pg = Factories.persistence.forPayroll();
    
	@Override
	public List<PayrollDto> execute() throws BusinessException {
		
		if (pg.alreadyGeneratedForPrevMonthof(LocalDate.now())) {
			return new ArrayList<PayrollDto>();
		}
		List<PayrollRecord> list = pg.generateForPrevMonth();
		for (PayrollRecord record : list) {
		    if (record.id == null || record.id.isBlank()) {
		    	record.id = java.util.UUID.randomUUID().toString();
		    }
		    if (record.version == 0) {
		    	record.version = 1L;
		    }
		}
		return PayrollAssembler.toDtoList(list);
	}

}
