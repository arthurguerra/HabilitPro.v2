package habilitpro.model.dao;

import habilitpro.enums.PerfilAcessoEnum;
import habilitpro.model.persistence.PerfilAcesso;

import javax.persistence.EntityManager;
import java.util.List;

public class PerfilAcessoDao {

    private EntityManager entityManager;

    public PerfilAcessoDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(PerfilAcesso perfilAcesso) {
        this.entityManager.persist(perfilAcesso);
    }

    public void delete(PerfilAcesso perfilAcesso) {
        this.entityManager.remove(perfilAcesso);
    }

    public PerfilAcesso getById(Long id) {
        return this.entityManager.find(PerfilAcesso.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<PerfilAcesso> listAll(){
        String sql = "SELECT * FROM perfilacesso";
        return this.entityManager.createNativeQuery(sql , PerfilAcesso.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PerfilAcesso> listByUsuario(Long usuarioId) {
        String sql = "SELECT * FROM perfilacesso WHERE usuario_id=:usuario_id";
        return this.entityManager.createNativeQuery(sql, PerfilAcesso.class)
                .setParameter("usuario_id", usuarioId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<PerfilAcesso> listByTipoDePerfilAcesso(String nomePerfilAcesso) {
        String sql = "SELECT * FROM perfilacesso WHERE perfilacessoenum=:perfilacessoenum";
        return this.entityManager.createNativeQuery(sql, PerfilAcesso.class)
                .setParameter("perfilacessoenum", nomePerfilAcesso.toLowerCase())
                .getResultList();
    }

}
