package uo.ri.conf;

import uo.ri.cws.application.persistence.PersistenceFactory;
import uo.ri.cws.application.service.ServiceFactory;

public class Factories {

    public static ServiceFactory service = new ServiceFactory();
    public static PersistenceFactory persistence = new PersistenceFactory();

    public static void close() {
    }

}
