package habilitpro.services;

import habilitpro.model.dao.ModuloDao;
import habilitpro.model.persistence.Modulo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class ModuloService {

    private final Logger LOG = LogManager.getLogger(ModuloService.class);

    private EntityManager entityManager;

    private ModuloDao moduloDao;

    private TrilhaService trilhaService;

    public ModuloService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.moduloDao = new ModuloDao(entityManager);
    }

    public void create(Modulo modulo) {
        this.LOG.info("Preparando para a criação do Módulo");
        if(modulo == null ){
            this.LOG.info("O Módulo informado está nulo!");
            throw new RuntimeException("Module is null!");
        }
        try {
            getBeginTransaction();
            this.moduloDao.create(modulo);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao Criar Módulo: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Módulo criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Módulo");
        if (id == null){
            this.LOG.error("O ID do Módulo está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Modulo modulo = this.moduloDao.getById(id);
        validaModuloNulo(modulo);
        this.LOG.info("Módulo encontrado com sucesso!");
        if (moduloDao.verificaSeModuloPossuiAvaliacoesAtivas(id)) {
            this.LOG.error("O Módulo não pode ser excluido porque possui Avaliações ativas relacionadas!");
            throw new PersistenceException("The Module cannot be excluded because it has active related Assessments!");
        }

        getBeginTransaction();
        this.moduloDao.delete(modulo);
        commitAndCloseTransaction();
        this.LOG.info("Módulo deletado com sucesso!");
    }

    public void update(Modulo novoModulo, Long moduloId) {
        this.LOG.info("Preparando para atualizar Módulo");
        if (novoModulo == null || moduloId == null){
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null");
        }
        Modulo modulo = this.moduloDao.getById(moduloId);
        validaModuloNulo(modulo);
        this.LOG.info("Módulo encontrado com sucesso!");

        getBeginTransaction();
        modulo.setNome(novoModulo.getNome());
        modulo.setHabilidades(novoModulo.getHabilidades());
        modulo.setTarefaValidacao(novoModulo.getTarefaValidacao());
        modulo.setPrazoLimite(novoModulo.getPrazoLimite());
        commitAndCloseTransaction();
        this.LOG.info("Ocupação atualizada com sucesso!");
    }

    public List<Modulo> listAll() {
        this.LOG.info("Preparando para listar Módulos");
        List<Modulo> modulos = this.moduloDao.listAll();
        if (modulos == null ) {
            this.LOG.info("Nenhum Módulo encontrado!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+modulos.size()+" Módulos");
        return modulos;
    }

    public Modulo getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Modulo modulo = this.moduloDao.getById(id);
        if(modulo == null) {
            this.LOG.error("Não foi encontrado o Módulo com ID " + id);
            throw new EntityNotFoundException("Module not found!");
        }
        return modulo;
    }

    public List<Modulo> listByTrilha(Long trilhaId) {
        if(trilhaId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null!");
        }
        this.LOG.info("Preparando para buscar Módulos pertencentes à Trilha de ID "+trilhaId);
        List<Modulo> modulos = this.moduloDao.listByTrilha(trilhaId);
        if (modulos == null) {
            this.LOG.info("Nenhum Módulo encontrado para a Trilha de ID "+trilhaId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+modulos.size()+" Módulos para a Trilha de ID" + trilhaId);
        return modulos;
    }

    private void validaModuloNulo(Modulo modulo) {
        if (modulo == null) {
            this.LOG.error("O Módulo não Existe!");
            throw new EntityNotFoundException("Module not found!");
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
