package uo.ri.cws.application.service.payroll.crud.commands;

import java.util.ArrayList;
import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollRecord;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollSummaryRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollAssembler;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.util.exception.BusinessException;

public class FindAllSummarized implements Command<List<PayrollSummaryDto>> {

	private PayrollGateway pg = Factories.persistence.forPayroll();
	
	@Override
	public List<PayrollSummaryDto> execute() throws BusinessException {
		List<PayrollRecord> op = pg.findAll();

		if (op.isEmpty()) {
			return new ArrayList<PayrollSummaryDto>();
		}
		//Converts Records to summaryRecords
		List<PayrollSummaryRecord> ps = PayrollAssembler.
				toSummaryRecordList2(op);
		//Converts summaryRecords to summaryDtos
		return PayrollAssembler.toSummaryDtoList(ps);
	}

}
