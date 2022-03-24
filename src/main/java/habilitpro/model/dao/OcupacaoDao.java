package habilitpro.model.dao;

import habilitpro.model.persistence.Ocupacao;
import habilitpro.model.persistence.Trilha;

import javax.persistence.EntityManager;
import java.util.List;

public class OcupacaoDao {

    private EntityManager entityManager;

    public OcupacaoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Ocupacao findByName(String nome) {
        String sql = "SELECT * FROM Ocupacao WHERE LOWER(nome) =:nome";
        return (Ocupacao) this.entityManager.createNativeQuery(sql, Ocupacao.class)
                .setParameter("nome", nome.toLowerCase())
                .getSingleResult();
    }

    public void create(Ocupacao ocupacao) {
        this.entityManager.persist(ocupacao);
    }

    public void delete(Ocupacao ocupacao) {
        this.entityManager.remove(ocupacao);
    }

    public Ocupacao getById(Long id) {
        return this.entityManager.find(Ocupacao.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Ocupacao> listAll() {
        String sql = "SELECT * FROM Ocupacao";
        return this.entityManager.createNativeQuery(sql, Ocupacao.class)
                .getResultList();
    }
}