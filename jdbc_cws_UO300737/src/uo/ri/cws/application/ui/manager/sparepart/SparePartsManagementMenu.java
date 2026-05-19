package uo.ri.cws.application.ui.manager.sparepart;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class SparePartsManagementMenu extends BaseMenu {

    public SparePartsManagementMenu() {
        menuOptions = new Object[][] { { "Manager > Parts management", null },

                { "Spare parts management", NotYetImplementedAction.class },
                { "Providers management", NotYetImplementedAction.class },
                { "Supplies management", NotYetImplementedAction.class },
                { "Orders management", NotYetImplementedAction.class }, };
    }

}
