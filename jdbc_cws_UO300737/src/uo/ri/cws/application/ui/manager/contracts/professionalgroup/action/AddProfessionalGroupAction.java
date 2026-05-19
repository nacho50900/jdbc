package uo.ri.cws.application.ui.manager.contracts.professionalgroup.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalGroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class AddProfessionalGroupAction implements Action {

    @Override
    public void execute() throws BusinessException {

    	ProfessionalGroupDto dto = new ProfessionalGroupDto();
        dto.name = Console.readString("Professional group name");
        dto.trienniumPayment = Console.readDouble("Triennium payment");
        dto.productivityRate = Console.readDouble("Productivity rate");

        ProfessionalGroupCrudService mcs = Factories.service.forProfessionalGroupCrudService();
    	dto = mcs.create(dto);

        Console.println("Professional group "+ dto.name + " registered");
    }
}