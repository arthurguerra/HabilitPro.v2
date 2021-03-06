package habilitpro.model.dao;

import habilitpro.model.persistence.Modulo;
import habilitpro.model.persistence.Trilha;

import javax.persistence.EntityManager;
import java.util.List;

public class TrilhaDao {

    private EntityManager entityManager;

    public TrilhaDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Trilha trilha) {
        this.entityManager.persist(trilha);
    }

    public void delete(Trilha trilha) {
        this.entityManager.remove(trilha);
    }

    public Trilha getById(Long id) {
        return this.entityManager.find(Trilha.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listAll() {
        String sql = "SELECT * FROM Trilha";
        return this.entityManager.createNativeQuery(sql, Trilha.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listByEmpresa(Long empresaId) {
        String sql = "SELECT * FROM Trilha WHERE empresa_id=:empresa_id";
        return this.entityManager.createNativeQuery(sql, Trilha.class)
                .setParameter("empresa_id", empresaId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Trilha> listByOcupacao(Long ocupacaoId) {
        String sql = "SELECT * FROM Trilha WHERE ocupacao_id=:ocupacao_id";
        return this.entityManager.createNativeQuery(sql, Trilha.class)
                .setParameter("ocupacao_id", ocupacaoId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public boolean verificaSeTrilhaPossuiModulosAtivos(Long id) {
        String sql = "SELECT * FROM Modulo WHERE trilha_id=:trilha_id";
        List<Modulo> modulos = this.entityManager.createNativeQuery(sql, Modulo.class)
                .setParameter("trilha_id", id)
                .getResultList();
        return modulos != null;
    }
}
