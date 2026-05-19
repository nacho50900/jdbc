package uo.ri.cws.application.ui.manager;

import uo.ri.cws.application.ui.manager.contracts.ContractsManagementMenu;
import uo.ri.cws.application.ui.manager.mechanic.MechanicMenu;
import uo.ri.cws.application.ui.manager.payroll.PayrollManagementMenu;
import uo.ri.cws.application.ui.manager.sparepart.SparePartsManagementMenu;
import uo.ri.cws.application.ui.manager.vehicletype.VehicleTypesMenu;
import uo.ri.util.menu.BaseMenu;

public class MainMenu extends BaseMenu {

    public MainMenu() {
        menuOptions = new Object[][] { { "Manager", null },

                { "Mechanics management", MechanicMenu.class },
                { "Contracts management", ContractsManagementMenu.class },
                { "Payrolls management", PayrollManagementMenu.class },
                { "Spare parts management", SparePartsManagementMenu.class },
                { "Vehicle types management", VehicleTypesMenu.class }, };
    }

    public static void main(String[] args) {
        new MainMenu().execute();
    }

}
