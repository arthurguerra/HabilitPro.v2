package habilitpro.model.dao;

import habilitpro.model.persistence.Empresa;
import habilitpro.model.persistence.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class EmpresaDao {

    private EntityManager entityManager;

    public EmpresaDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Empresa empresa) {
        this.entityManager.persist(empresa);
    }

    public void delete(Empresa empresa) {
        this.entityManager.remove(empresa);
    }

    public Empresa getById(Long id) {
        return this.entityManager.find(Empresa.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> listAll() {
        String sql = "SELECT * FROM Empresa";
        return this.entityManager.createNativeQuery(sql, Empresa.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Empresa> listByName(String nome) {
        String sql = "SELECT * FROM Empresa WHERE LOWER(nome)=:nome";
        return this.entityManager.createNativeQuery(sql, Empresa.class)
                .setParameter("nome", nome.toLowerCase())
                .getResultList();
    }

    public Empresa findByCnpj(String cnpj) {
        String sql = "SELECT * FROM Empresa WHERE cnpj =:cnpj";
        return (Empresa) this.entityManager.createNativeQuery(sql, Empresa.class)
                .setParameter("cnpj", cnpj)
                .getSingleResult();
    }
}
