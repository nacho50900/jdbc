package uo.ri.cws.application.ui.manager.contracts.professionalgroup;

import uo.ri.cws.application.ui.manager.contracts.professionalgroup.action.AddProfessionalGroupAction;
import uo.ri.cws.application.ui.manager.contracts.professionalgroup.action.DeleteProfessionalGroupAction;
import uo.ri.cws.application.ui.manager.contracts.professionalgroup.action.FindProfessionalGroupByNameAction;
import uo.ri.cws.application.ui.manager.contracts.professionalgroup.action.ListAllProfessionalGroupsAction;
import uo.ri.cws.application.ui.manager.contracts.professionalgroup.action.UpdateProfessionalGroupAction;
import uo.ri.util.menu.BaseMenu;

public class ProfessionalGroupsMenu extends BaseMenu {
    {
        menuOptions = new Object[][] {
                { "Manager > Contracts management > Professional groups",
                        null },

                { "Add professional group", AddProfessionalGroupAction.class },
                { "Delete professional group",
                        DeleteProfessionalGroupAction.class },
                { "Update professional group",
                        UpdateProfessionalGroupAction.class },
                { "Find professional group by name",
                        FindProfessionalGroupByNameAction.class },
                { "List all professional groups",
                        ListAllProfessionalGroupsAction.class }, };

    }
}
