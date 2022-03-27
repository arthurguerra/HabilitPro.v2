package habilitpro.services;

import habilitpro.model.dao.PerfilAcessoDao;
import habilitpro.model.persistence.PerfilAcesso;
import habilitpro.model.persistence.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PerfilAcessoService {

    private final Logger LOG = LogManager.getLogger(PerfilAcessoService.class);

    private EntityManager entityManager;

    private PerfilAcessoDao perfilAcessoDao;

    private UsuarioService usuarioService;

    public PerfilAcessoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.perfilAcessoDao = new PerfilAcessoDao(entityManager);
        this.usuarioService = new UsuarioService(entityManager);
    }

    public void create(PerfilAcesso perfilAcesso) {
        this.LOG.info("Preparando para criar Perfil de Acesso");
        validaPerfilAcessoNulo(perfilAcesso);
        Long usuario_id = perfilAcesso.getUsuario().getId();
        this.LOG.info("Buscando se já existe o Usuário: "+perfilAcesso.getUsuario().getNome());
        Usuario usuario = this.usuarioService.getById(usuario_id);
        if (usuario != null) {
            this.LOG.info("Usuário "+usuario.getNome()+" encontrado no banco!");
            perfilAcesso.setUsuario(usuario);
        }
        try {
            getBeginTransaction();
            this.perfilAcessoDao.create(perfilAcesso);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Perfil de Acesso: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Perfil de Acesso criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Perfil de Acesso");
        if (id == null) {
            this.LOG.error("O ID da Trilha informada está nulo!");
            throw new RuntimeException("The ID is Null");
        }
        PerfilAcesso perfilAcesso = this.perfilAcessoDao.getById(id);
        validaPerfilAcessoNulo(perfilAcesso);
        this.LOG.info("Perfil de Acesso encontrado com sucesso!");
        try {
            getBeginTransaction();
            this.perfilAcessoDao.delete(perfilAcesso);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao Excluir Perfil de Acesso: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Perfil de Acesso deletado com sucesso!");
    }

    public void update(PerfilAcesso novoPerfil, Long perfilId) {
        this.LOG.info("Preparando para Atualizar Perfil de Acesso");
        if (novoPerfil == null || perfilId == null){
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null");
        }
        PerfilAcesso perfilAcesso = this.perfilAcessoDao.getById(perfilId);
        validaPerfilAcessoNulo(perfilAcesso);
        this.LOG.info("Perfil de Acesso encontrado com sucesso!");

        getBeginTransaction();
        perfilAcesso.setUsuario(novoPerfil.getUsuario());
        perfilAcesso.setPerfilAcesso(novoPerfil.getPerfilAcesso());
        commitAndCloseTransaction();
        this.LOG.info("Perfil de Acesso atualizado com sucesso!");
    }

    public List<PerfilAcesso> listAll() {
        this.LOG.info("Preparando para listar Perfis de Acesso");
        List<PerfilAcesso> perfis = this.perfilAcessoDao.listAll();
        if (perfis == null) {
            this.LOG.info("Nenhum Perfil de Acesso foi encontrado!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+perfis.size()+" Perfis de Acesso");
        return perfis;
    }

    public PerfilAcesso getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        PerfilAcesso perfilAcesso = this.perfilAcessoDao.getById(id);
        if(perfilAcesso == null) {
            this.LOG.error("Não foi encontrada o Perfil de Acesso com ID " + id);
            throw new EntityNotFoundException("Access Profile not found!");
        }
        return perfilAcesso;
    }

    public List<PerfilAcesso> listByUsuario(Long usuarioId) {
        this.LOG.info("Preparando para buscar Perfis de Acesso do Usuário com ID "+usuarioId);
        if(usuarioId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        List<PerfilAcesso> perfis = this.perfilAcessoDao.listByUsuario(usuarioId);
        if (perfis == null) {
            this.LOG.info("Nenhum Perfil de Acesso encontrado para o Usuário de ID "+usuarioId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+perfis.size()+" Perfis de Acesso para o Usuário com ID "+usuarioId);
        return perfis;
    }

    public List<PerfilAcesso> listByTipoDePerfilAcesso(String perfilAcesso) {
        this.LOG.info("Preparando para buscar Perfis de Acesso do tipo: "+perfilAcesso);
        if(perfilAcesso == null) {
            this.LOG.error("O nome do Perfil de Acesso não pode ser nulo!");
            throw new RuntimeException("Name is null!");
        }
        List<PerfilAcesso> perfis = this.perfilAcessoDao.listByTipoDePerfilAcesso(perfilAcesso);
        if(perfis == null) {
            this.LOG.info("Nenhum Perfil de Acesso encontrado do tipo: "+perfilAcesso);
            return null;
        }
        this.LOG.info("Foram encontrados "+perfis.size()+" Perfis de Acesso do tipo "+perfilAcesso);
        return perfis;
    }

    private void validaPerfilAcessoNulo(PerfilAcesso perfilAcesso) {
        if (perfilAcesso == null) {
            this.LOG.error("O Perfil de Acesso não Existe!");
            throw new EntityNotFoundException("Access Profile not found!");
        }
    }

    private void getBeginTransaction() {
        this.LOG.info("Abrindo Transação com o banco de dados...");
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        this.LOG.info("Commitando e Fechando transação com o banco de dados");
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
