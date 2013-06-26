package main.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * delivers Entitymanagerfactory.
 * @author christianroser
 *
 */
@WebListener
public class EMF implements ServletContextListener {

    private static EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	 emf = Persistence.createEntityManagerFactory("PU");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        emf.close();
    }

    /**
     * creates em.
     * @return entitymanager
     */
    public static EntityManager createEntityManager() {
        if (emf == null) {
        	emf = Persistence.createEntityManagerFactory("PU");
        }

        return emf.createEntityManager();
    }

}
