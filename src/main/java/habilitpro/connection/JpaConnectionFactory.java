package habilitpro.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaConnectionFactory {

    private EntityManagerFactory factory;

    public JpaConnectionFactory() {
        this.factory = Persistence.createEntityManagerFactory("habilitpro");
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
