package habilitpro.model.dao;

import habilitpro.model.persistence.Funcao;

import javax.persistence.EntityManager;
import java.util.List;

public class FuncaoDao {

    private EntityManager entityManager;

    public FuncaoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Funcao funcao) {
        this.entityManager.persist(funcao);
    }

    public void delete(Funcao funcao) {
        this.entityManager.remove(funcao);
    }

    public Funcao getById(Long id) {
        return this.entityManager.find(Funcao.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Funcao> listAll() {
        String sql = "SELECT * FROM Funcao";
        return this.entityManager.createNativeQuery(sql, Funcao.class)
                .getResultList();
    }

    public Funcao findByName(String nome) {
        String sql = "SELECT * FROM Funcao WHERE LOWER(nome)=:nome";
        return (Funcao) this.entityManager.createNativeQuery(sql, Funcao.class)
                .setParameter("nome", nome)
                .getSingleResult();
    }
}
