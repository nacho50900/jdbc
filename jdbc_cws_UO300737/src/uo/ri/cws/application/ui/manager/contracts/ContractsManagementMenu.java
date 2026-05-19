package uo.ri.cws.application.ui.manager.contracts;

import uo.ri.cws.application.ui.manager.contracts.contract.ContractsMenu;
import uo.ri.cws.application.ui.manager.contracts.contracttype.ContractTypesMenu;
import uo.ri.cws.application.ui.manager.contracts.professionalgroup.ProfessionalGroupsMenu;
import uo.ri.util.menu.BaseMenu;

public class ContractsManagementMenu extends BaseMenu {
    {
        menuOptions = new Object[][] {
                { "Manager > Contracts management", null },

                { "Contracts", ContractsMenu.class },
                { "ContractTypes", ContractTypesMenu.class },
                { "Professional groups", ProfessionalGroupsMenu.class }, };
    }
}