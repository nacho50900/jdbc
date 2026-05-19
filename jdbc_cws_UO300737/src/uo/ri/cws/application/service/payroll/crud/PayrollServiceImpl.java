package uo.ri.cws.application.service.payroll.crud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.commands.DeleteLastGenerated;
import uo.ri.cws.application.service.payroll.crud.commands.DeleteLastGeneratedOfMechanicId;
import uo.ri.cws.application.service.payroll.crud.commands.GenerateForPreviousMonth;
import uo.ri.cws.application.service.payroll.crud.commands.GenerateForPreviousMonthOf;
import uo.ri.cws.application.service.payroll.crud.commands.ListAllSummarized;
import uo.ri.cws.application.service.payroll.crud.commands.ListByPayrollId;
import uo.ri.cws.application.service.payroll.crud.commands.ListSummarizedByMechanicId;
import uo.ri.cws.application.service.payroll.crud.commands.ListSummarizedByProfessionalGroupName;
import uo.ri.util.exception.BusinessException;

public class PayrollServiceImpl implements PayrollService {

	@Override
	public List<PayrollDto> generateForPreviousMonth() 
			throws BusinessException {
		GenerateForPreviousMonth gm = new GenerateForPreviousMonth();
		return gm.execute();
	}

	@Override
	public List<PayrollDto> generateForPreviousMonthOf(LocalDate present) 
			throws BusinessException {
		GenerateForPreviousMonthOf gm = new GenerateForPreviousMonthOf(present);
		return gm.execute();
	}

	@Override
	public void deleteLastGeneratedOfMechanicId(String mechanicId) 
			throws BusinessException {
		DeleteLastGeneratedOfMechanicId dl = 
				new DeleteLastGeneratedOfMechanicId(mechanicId);
		dl.execute();
	}

	@Override
	public int deleteLastGenerated() throws BusinessException {
		DeleteLastGenerated dl = new DeleteLastGenerated();
		return dl.execute();
	}

	@Override
	public Optional<PayrollDto> findById(String id) throws BusinessException {
		ListByPayrollId li = new ListByPayrollId(id);
		PayrollDto op  = li.execute();
		if (op == null) {
			return Optional.empty();
		}
		return Optional.of(op);
	}

	@Override
	public List<PayrollSummaryDto> findAllSummarized() 
			throws BusinessException {
		ListAllSummarized ls = new ListAllSummarized();
		return ls.execute();
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByMechanicId(String id) 
			throws BusinessException {
		ListSummarizedByMechanicId ls = new ListSummarizedByMechanicId(id);
		return ls.execute();
	}

	@Override
	public List<PayrollSummaryDto> findSummarizedByProfessionalGroupName(
			String name) throws BusinessException {
		ListSummarizedByProfessionalGroupName ls = 
				new ListSummarizedByProfessionalGroupName(name);
		return ls.execute();
	}

}
