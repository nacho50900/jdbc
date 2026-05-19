package uo.ri.cws.application.ui.manager.contracts.contract;

import uo.ri.cws.application.ui.manager.contracts.contract.action.AddContractAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.DeleteContractAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.FinishContractAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.ListAllContractsAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.ListContractsOfMechanicAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.ShowContractDetailsAction;
import uo.ri.cws.application.ui.manager.contracts.contract.action.UpdateContractAction;
import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class ContractsMenu extends BaseMenu {
    {
        menuOptions = new Object[][] {
                { "Administrator > Contract management", null },

                { "List mechanics", NotYetImplementedAction.class },
                { "Add contract", AddContractAction.class },
                { "Update contract", UpdateContractAction.class },
                { "Delete contract", DeleteContractAction.class },
                { "Finish contract", FinishContractAction.class },
                { "Show contract details", ShowContractDetailsAction.class },
                { "List contracts of a mechanic",
                        ListContractsOfMechanicAction.class },
                { "List all contracts", ListAllContractsAction.class } };
    }
}