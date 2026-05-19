package uo.ri.cws.application.ui.manager.contracts.professionalgroup.action;

import java.util.List;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.cws.application.ui.util.Printer;
import uo.ri.util.console.Console;
import uo.ri.util.menu.Action;

public class ListAllProfessionalGroupsAction implements Action {

    @Override
    public void execute() throws Exception {

        ProfessionalGroupCrudService mcs = Factories.service.forProfessionalGroupCrudService();
        List<ProfessionalGroupDto> groups = mcs.findAll();

        if (groups.isEmpty()) {
            Console.println("No professional groups found");
            return;
        }
        
        for (ProfessionalGroupDto dto : groups) {
            Printer.printProfessionalGroup(dto);
        }
    }
}