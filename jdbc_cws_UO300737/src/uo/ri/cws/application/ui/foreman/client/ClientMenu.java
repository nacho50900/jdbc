package uo.ri.cws.application.ui.foreman.client;

import uo.ri.util.menu.BaseMenu;
import uo.ri.util.menu.NotYetImplementedAction;

public class ClientMenu extends BaseMenu {

    public ClientMenu() {
        menuOptions = new Object[][] { { "Foreman > Client management", null },

                { "Register a client", NotYetImplementedAction.class },
                { "Update a client", NotYetImplementedAction.class },
                { "Disable a client", NotYetImplementedAction.class },
                { "List all clients", NotYetImplementedAction.class }, };
    }

}
