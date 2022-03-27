package habilitpro.model.dao;

import habilitpro.model.persistence.PerfilAcesso;
import habilitpro.model.persistence.Usuario;

import javax.persistence.EntityManager;
import java.util.List;

public class UsuarioDao {

    private EntityManager entityManager;

    public UsuarioDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void create(Usuario usuario) {
        this.entityManager.persist(usuario);
    }

    public void delete(Usuario usuario) {
        this.entityManager.remove(usuario);
    }

    public Usuario getById(Long id) {
        return this.entityManager.find(Usuario.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listAll() {
        String sql = "SELECT * FROM Usuario";
        return this.entityManager.createNativeQuery(sql, Usuario.class)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> listByName(String nome) {
        String sql = "SELECT * FROM Usuario WHERE nome=:nome";
        return this.entityManager.createNativeQuery(sql, Usuario.class)
                .setParameter("nome", nome)
                .getResultList();
    }

    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE email =:email";
        return (Usuario) this.entityManager.createNativeQuery(sql, Usuario.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public boolean verificaSeUsuarioPossuiPerfisAtivos(Long usuarioId) {
        String sql = "SELECT * FROM perfilacesso WHERE usuario_id=:usuario_id";
        List<PerfilAcesso> perfis = this.entityManager.createNativeQuery(sql, PerfilAcesso.class)
                .setParameter("usuario_id", usuarioId)
                .getResultList();
        return perfis != null;
    }
}
