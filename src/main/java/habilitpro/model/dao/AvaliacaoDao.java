package habilitpro.model.dao;

import habilitpro.model.persistence.Avaliacao;

import javax.persistence.EntityManager;
import java.util.List;

public class AvaliacaoDao {

    private EntityManager entityManager;

    public AvaliacaoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Avaliacao avaliacao) {
        this.entityManager.persist(avaliacao);
    }

    public void delete(Avaliacao avaliacao) {
        this.entityManager.remove(avaliacao);
    }

    public Avaliacao getById(Long id) {
        return this.entityManager.find(Avaliacao.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Avaliacao> listAll() {
        String sql = "SELECT * FROM Avaliacao";
        return this.entityManager.createNativeQuery(sql, Avaliacao.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Avaliacao> listByModulo(Long moduloId) {
        String sql = "SELECT * FROM Avaliacao WHERE modulo_id=:modulo_id";
        return this.entityManager.createNativeQuery(sql, Avaliacao.class)
                .setParameter("modulo_id", moduloId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Avaliacao> listByTrabalhador(Long trabalhadorId) {
        String sql = "SELECT * FROM Avaliacao WHERE trabalhador_id=:trabalhador_id";
        return this.entityManager.createNativeQuery(sql, Avaliacao.class)
                .setParameter("trabalhador_id", trabalhadorId)
                .getResultList();
    }
}
