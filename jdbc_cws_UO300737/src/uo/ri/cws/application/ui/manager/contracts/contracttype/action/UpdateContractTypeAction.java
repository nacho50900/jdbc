package uo.ri.cws.application.ui.manager.contracts.contracttype.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService;
import uo.ri.cws.application.service.contracttype.ContractTypeCrudService.ContractTypeDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateContractTypeAction implements Action {

    @Override
    public void execute() throws BusinessException {
        String name = Console.readString("Contract type name");

        ContractTypeCrudService service =
                Factories.service.forContractTypeCrudService();
        Optional<ContractTypeDto> existing = service.findByName(name);

        if (existing.isEmpty()) {
            Console.println("Contract type not found");
            return;
        }

        ContractTypeDto dto = existing.get();
        Console.println("Current compensation days: " + dto.compensationDays);
        dto.compensationDays =
                Console.readDouble("New compensation days");

        service.update(dto);
        Console.println("Contract type updated");
    }

}
