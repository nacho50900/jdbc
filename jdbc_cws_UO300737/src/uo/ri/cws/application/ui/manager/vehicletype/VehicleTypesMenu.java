package uo.ri.cws.application.ui.manager.vehicletype;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class VehicleTypesMenu extends BaseMenu {

    public VehicleTypesMenu() {
        menuOptions = new Object[][] {
                { "Manager > Vehicle type management", null },

                { "Add vehicle type ", NotYetImplementedAction.class },
                { "Update vehicle type ", NotYetImplementedAction.class },
                { "Delete vehicle type ", NotYetImplementedAction.class },
                { "List vehicle types", NotYetImplementedAction.class }, };
    }

}
