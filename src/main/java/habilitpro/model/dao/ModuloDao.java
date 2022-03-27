package habilitpro.model.dao;

import habilitpro.model.persistence.Avaliacao;
import habilitpro.model.persistence.Modulo;

import javax.persistence.EntityManager;
import java.util.List;

public class ModuloDao {

    private EntityManager entityManager;

    public ModuloDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Modulo modulo) {
        this.entityManager.persist(modulo);
    }

    public void delete(Modulo modulo) {
        this.entityManager.remove(modulo);
    }

    public Modulo getById(Long id) {
        return this.entityManager.find(Modulo.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Modulo> listAll() {
        String sql = "SELECT * FROM Modulo";
        return this.entityManager.createNativeQuery(sql, Modulo.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Modulo> listByTrilha(Long trilhaId) {
        String sql = "SELECT * FROM Modulo WHERE trilha_id=:trilha_id";
        return this.entityManager.createNativeQuery(sql, Modulo.class)
                .setParameter("trilha_id", trilhaId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public boolean verificaSeModuloPossuiAvaliacoesAtivas(Long id) {
        String sql = "SELECT * FROM Avaliacao WHERE modulo_id=:modulo_id";
        List<Avaliacao> avaliacoes = this.entityManager.createNativeQuery(sql, Avaliacao.class)
                .setParameter("modulo_id", id)
                .getResultList();
        return avaliacoes != null;
    }

}
