package habilitpro.model.dao;

import habilitpro.model.persistence.Setor;

import javax.persistence.EntityManager;
import java.util.List;

public class SetorDao {

    private EntityManager entityManager;

    public SetorDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Setor setor) {
        this.entityManager.persist(setor);
    }

    public void delete(Setor setor) {
        this.entityManager.remove(setor);
    }

    public Setor getById(Long id) {
        return this.entityManager.find(Setor.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Setor> listAll() {
        String sql = "SELECT * FROM Setor";
        return this.entityManager.createNativeQuery(sql, Setor.class)
                .getResultList();
    }

    public Setor findByName(String nome) {
        String sql = "SELECT * FROM Setor WHERE LOWER(nome) =:nome";
        return (Setor) this.entityManager.createNativeQuery(sql, Setor.class)
                .setParameter("nome", nome.toLowerCase())
                .getSingleResult();
    }
}
