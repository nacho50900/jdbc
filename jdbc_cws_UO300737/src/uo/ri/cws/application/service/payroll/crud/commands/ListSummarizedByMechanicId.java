package uo.ri.cws.application.service.payroll.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.mechanic.MechanicGateway;
import uo.ri.cws.application.persistence.mechanic.MechanicRecord;
import uo.ri.cws.application.persistence.payroll.PayrollAssembler;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollSummaryRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class ListSummarizedByMechanicId 
	implements Command<List<PayrollSummaryDto>> {

	private String mechanicId;
	private PayrollGateway pg = Factories.persistence.forPayroll();
	private MechanicGateway mg = Factories.persistence.forMechanic();
	
	public ListSummarizedByMechanicId(String mechanicId) {
		ArgumentChecks.isNotNull(mechanicId, "mechanicId cannot be null");
		ArgumentChecks.isNotBlank(mechanicId, "mechanicId cannot be blank");
		this.mechanicId = mechanicId;
	}
	
	@Override
	public List<PayrollSummaryDto> execute() throws BusinessException {
		Optional<MechanicRecord> om = mg.findById(mechanicId);
		BusinessChecks.exists(om, "The mechanic does not exist");
		
		List<PayrollSummaryRecord> lp = pg.findpayrollsByMechanicId(mechanicId);

		if (lp.isEmpty()) {
			return new ArrayList<PayrollSummaryDto>();
		}

		//Converts summaryRecords to summaryDtos
		return PayrollAssembler.toSummaryDtoList(lp);
	}

}
