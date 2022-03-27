package habilitpro.model.dao;

import habilitpro.model.persistence.Avaliacao;
import habilitpro.model.persistence.Trabalhador;

import javax.persistence.EntityManager;
import java.util.List;

public class TrabalhadorDao {

    private EntityManager entityManager;

    public TrabalhadorDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Trabalhador trabalhador) {
        this.entityManager.persist(trabalhador);
    }

    public void delete(Trabalhador trabalhador) {
        this.entityManager.remove(trabalhador);
    }

    public Trabalhador getById(Long id) {
        return this.entityManager.find(Trabalhador.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listAll() {
        String sql = "SELECT * FROM Trabalhador";
        return this.entityManager.createNativeQuery(sql, Trabalhador.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listByEmpresa(Long empresaId) {
        String sql = "SELECT * FROM Trabalhador WHERE empresa_id=:empresa_id";
        return this.entityManager.createNativeQuery(sql, Trabalhador.class)
                .setParameter("empresa_id", empresaId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listByFuncao(Long funcaoId) {
        String sql = "SELECT * FROM Trabalhador WHERE funcao_id=:funcao_id";
        return this.entityManager.createNativeQuery(sql, Trabalhador.class)
                .setParameter("funcao_id", funcaoId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trabalhador> listBySetor(Long setorId) {
        String sql = "SELECT * FROM Trabalhador WHERE setor_id=:setor_id";
        return this.entityManager.createNativeQuery(sql, Trabalhador.class)
                .setParameter("setor_id", setorId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public boolean verificaSeTrabalhadorPossuiAvaliacoesAtivas(Long id) {
        String sql = "SELECT * FROM Avaliacao WHERE trabalhador_id=:trabalhador_id";
        List<Avaliacao> avaliacoes = this.entityManager.createNativeQuery(sql, Avaliacao.class)
                .setParameter("trabalhador_id", id)
                .getResultList();
        return avaliacoes != null;
    }

}
