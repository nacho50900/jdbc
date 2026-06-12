package uo.ri.cws.application.ui.manager.contracts.professionalgroup.action;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService;
import uo.ri.cws.application.service.profesionalgroup.ProfessionalGroupCrudService.ProfessionalGroupDto;
import uo.ri.util.console.Console;
import uo.ri.util.exception.BusinessException;
import uo.ri.util.menu.Action;

public class UpdateProfessionalGroupAction implements Action {

    @Override
    public void execute() throws BusinessException {
    	
    	ProfessionalGroupDto dto = new ProfessionalGroupDto();
    	
        dto.name = Console.readString("Professional group name");
        dto.trienniumPayment = Console.readDouble("Triennium payment");
        dto.productivityRate = Console.readDouble("Productivity rate");
        
        ProfessionalGroupCrudService mcs = Factories.service.forProfessionalGroupCrudService();
        
        
		try {
			mcs.update(dto);
	        Console.println("Professional group updated");
		} catch (BusinessException be) {
		    Console.println(be.getMessage());
		}
         
    }
}