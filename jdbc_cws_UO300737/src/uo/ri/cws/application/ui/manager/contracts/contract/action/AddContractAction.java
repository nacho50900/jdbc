package uo.ri.cws.application.ui.manager.contracts.contract.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.contract.ContractCrudService;
import uo.ri.cws.application.service.contract.ContractCrudService.ContractDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddContractAction implements Action {

    @Override
    public void execute() throws BusinessException {
        ContractDto dto = new ContractDto();

        dto.mechanic.nif = Console.readString("Mechanic NIF");

        Console.println("Contract types: PERMANENT  FIXED_TERM  SEASONAL");
        dto.contractType.name = Console.readString("Contract type name");

        Console.println("Professional groups: I  II  III  IV  V  VI  VII");
        dto.professionalGroup.name =
                Console.readString("Professional group name");

        dto.annualBaseSalary = Console.readDouble("Annual base salary");

        if ("FIXED_TERM".equalsIgnoreCase(dto.contractType.name)) {
            dto.endDate = Console.readDate("End date (yyyy-MM-dd)");
        }

        ContractCrudService service =
                Factories.service.forContractCrudService();
        
		try {
	        ContractDto created = service.create(dto);
	        Console.println("Contract registered, id: " + created.id);
	        Console.println("Start date: " + created.startDate);
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}

    }

}
