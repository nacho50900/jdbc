package uo.ri.cws.application.service.payroll.crud.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.persistence.payroll.PayrollAssembler;
import uo.ri.cws.application.persistence.payroll.PayrollGateway;
import uo.ri.cws.application.persistence.payroll.PayrollGateway.PayrollSummaryRecord;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupGateway;
import uo.ri.cws.application.persistence.professionalgroup.ProfessionalGroupRecord;
import uo.ri.cws.application.persistence.util.command.Command;
import uo.ri.cws.application.service.payroll.PayrollService.PayrollSummaryDto;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class FindSummarizedByProfessionalGroupName 
implements Command<List<PayrollSummaryDto>> {

	private String groupName;
	private PayrollGateway pg = Factories.persistence.forPayroll();
	private ProfessionalGroupGateway gg = 
			Factories.persistence.forProfessionalGroup();
	
	public FindSummarizedByProfessionalGroupName(String groupName) {
		ArgumentChecks.isNotNull(groupName, "groupName cannot be null");
		ArgumentChecks.isNotBlank(groupName, "groupName cannot be blank");
		this.groupName = groupName;
	}
	
	@Override
	public List<PayrollSummaryDto> execute() throws BusinessException {
		Optional<ProfessionalGroupRecord> om = gg.findByName(groupName);
		BusinessChecks.exists(om, "The professional group does not exist");
		
		List<PayrollSummaryRecord> lp = 
				pg.findPayrollsByProfessionalGroupName(groupName);

		if (lp.isEmpty()) {
			return new ArrayList<PayrollSummaryDto>();
		}

		//Converts summaryRecords to summaryDtos
		return PayrollAssembler.toSummaryDtoList(lp);
	}

}
