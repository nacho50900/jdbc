package uo.ri.cws.application.service.payroll.crud;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.cws.application.persistence.util.command.CommandExecutor;
import uo.ri.cws.application.service.payroll.PayrollService;
import uo.ri.cws.application.service.payroll.crud.commands.DeleteLastGenerated;
import uo.ri.cws.application.service.payroll.crud.commands.DeleteLastGeneratedOfMechanicId;
import uo.ri.cws.application.service.payroll.crud.commands.GenerateForPreviousMonth;
import uo.ri.cws.application.service.payroll.crud.commands.GenerateForPreviousMonthOf;
import uo.ri.cws.application.service.payroll.crud.commands.FindAllSummarized;
import uo.ri.cws.application.service.payroll.crud.commands.FindByPayrollId;
import uo.ri.cws.application.service.payroll.crud.commands.FindSummarizedByMechanicId;
import uo.ri.cws.application.service.payroll.crud.commands.FindSummarizedByProfessionalGroupName;
import uo.ri.util.exception.BusinessException;

public class PayrollServiceImpl implements PayrollService {

    private CommandExecutor executor = new CommandExecutor();

    @Override
    public List<PayrollDto> generateForPreviousMonth() throws BusinessException {
        return executor.execute(new GenerateForPreviousMonth());
    }

    @Override
    public List<PayrollDto> generateForPreviousMonthOf(LocalDate present)
            throws BusinessException {
        return executor.execute(new GenerateForPreviousMonthOf(present));
    }

    @Override
    public void deleteLastGeneratedOfMechanicId(String mechanicId)
            throws BusinessException {
        executor.execute(new DeleteLastGeneratedOfMechanicId(mechanicId));
    }

    @Override
    public int deleteLastGenerated() throws BusinessException {
        return executor.execute(new DeleteLastGenerated());
    }

    @Override
    public Optional<PayrollDto> findById(String id) throws BusinessException {
        return executor.execute(new FindByPayrollId(id));
    }

    @Override
    public List<PayrollSummaryDto> findAllSummarized() throws BusinessException {
        return executor.execute(new FindAllSummarized());
    }

    @Override
    public List<PayrollSummaryDto> findSummarizedByMechanicId(String id)
            throws BusinessException {
        return executor.execute(new FindSummarizedByMechanicId(id));
    }

    @Override
    public List<PayrollSummaryDto> findSummarizedByProfessionalGroupName(
            String name) throws BusinessException {
        return executor.execute(
                new FindSummarizedByProfessionalGroupName(name));
    }

}