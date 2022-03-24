package habilitpro.services;

import habilitpro.model.dao.TrilhaDao;
import habilitpro.model.persistence.Ocupacao;
import habilitpro.model.persistence.Trilha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class TrilhaService {

    private final Logger LOG = LogManager.getLogger(TrilhaService.class);

    private EntityManager entityManager;

    private TrilhaDao trilhaDao;

    public TrilhaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.trilhaDao = new TrilhaDao(entityManager);
    }

    private void validaTrilhaNula(Trilha trilha) {
        if (trilha == null) {
            this.LOG.error("A Trilha não Existe!");
            throw new EntityNotFoundException("Trail not found!");
        }
    }

    private void getBeginTransaction() {
        this.LOG.info("Abrindo Transação com o banco de dados...");
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        this.LOG.info("Commitando e Fechando transação com o banco de dados");
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
