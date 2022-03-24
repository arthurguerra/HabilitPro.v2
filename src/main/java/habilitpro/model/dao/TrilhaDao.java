package habilitpro.model.dao;

import habilitpro.model.persistence.Empresa;
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
        return this.entityManager.createNativeQuery(sql, Empresa.class)
                .setParameter("empresa_id", empresaId)
                .getResultList();
    }
}
