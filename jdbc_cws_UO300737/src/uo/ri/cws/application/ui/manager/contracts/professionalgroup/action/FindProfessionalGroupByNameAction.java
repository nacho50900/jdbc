package uo.ri.cws.application.ui.manager.contracts.professionalgroup.action;

import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class FindProfessionalGroupByNameAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
        String name = Console.readString("Professional group name");

        ProfessionalGroupCrudService mcs = Factories.service.forProfessionalGroupCrudService();
        Optional<ProfessionalGroupDto> op = mcs.findByName(name);
        if (op.isEmpty()) {
        	Console.println("The professional group not found");
        }
        ProfessionalGroupDto dto = op.get();
        Printer.printProfessionalGroup(dto);
    }
}