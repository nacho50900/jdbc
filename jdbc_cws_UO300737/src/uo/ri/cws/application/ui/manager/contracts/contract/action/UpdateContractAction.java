package uo.ri.cws.application.ui.manager.contracts.contract.action;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractSummaryDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractAction implements Action {

    @Override
    public void execute() throws BusinessException {
        ContractCrudService service =
                Factories.service.forContractCrudService();

        List<ContractSummaryDto> all = service.findAll();
        if (all.isEmpty()) {
            Console.println("No contracts found");
            return;
        }
        for (ContractSummaryDto c : all) {
            Printer.printContractSummary(c);
        }

        String id = Console.readString("Contract id to update");
        Optional<ContractDto> existing = service.findById(id);
        if (existing.isEmpty()) {
            Console.println("Contract not found");
            return;
        }

        ContractDto dto = existing.get();
        Console.println("Current annual base salary: "
                + dto.annualBaseSalary);
        Console.println("Current end date: " + dto.endDate);

        dto.annualBaseSalary =
                Console.readDouble("New annual base salary");
        dto.endDate = readOptionalDate("New end date (leave blank to clear)");

		try {
	        service.update(dto);
	        Console.println("Contract updated");
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
		
    }

    private LocalDate readOptionalDate(String msg) {
        while (true) {
            try {
                Console.print(msg + ": ");
                String input = Console.readString();
                if (input == null || input.isBlank()) {
                    return null;
                }
                return LocalDate.parse(input);
            } catch (Exception e) {
                Console.println("Invalid date, try again (yyyy-MM-dd)");
            }
        }
    }

}
