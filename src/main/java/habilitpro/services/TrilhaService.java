package habilitpro.services;

import habilitpro.model.dao.TrilhaDao;
import habilitpro.model.persistence.Ocupacao;
import habilitpro.model.persistence.Trilha;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrilhaService {

    private final Logger LOG = LogManager.getLogger(TrilhaService.class);

    private EntityManager entityManager;

    private TrilhaDao trilhaDao;

    private OcupacaoService ocupacaoService;

    private EmpresaService empresaService;

    public TrilhaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.trilhaDao = new TrilhaDao(entityManager);
        this.ocupacaoService = new OcupacaoService(entityManager);
        this.empresaService = new EmpresaService(entityManager);
    }

    public void create(Trilha trilha) {
        this.LOG.info("Preparando para a Criação da Trilha");
        if(trilha == null ){
            this.LOG.info("A Trilha informada está nula!");
            throw new RuntimeException("Trail is null!");
        }
//        String cnpjEmpresa = trilha.getEmpresa().getCnpj();
//        this.LOG.info("Buscando se já existe Empresa com CNPJ de: "+cnpjEmpresa);
//        Empresa empresa = this.empresaService.findByCnpj(cnpjEmpresa);
//        if(empresa != null) {
//            this.LOG.info("Empresa com CNPJ "+cnpjEmpresa+" encontrada no banco!");
//            trilha.setEmpresa(empresa);
//        }
        String nomeOcupacao = trilha.getOcupacao().getNome();
        this.LOG.info("Buscando se já existe a Ocupação: "+nomeOcupacao);
        Ocupacao ocupacao = this.ocupacaoService.findByName(nomeOcupacao);
        if(ocupacao != null) {
            this.LOG.info("Ocupação "+nomeOcupacao+" encontrada no banco!");
            trilha.setOcupacao(ocupacao);
        }
        try {
            getBeginTransaction();
            long contador = geraContador(trilha.getEmpresa().getId(), trilha.getOcupacao().getNome());
            String nome = String.format("%s - %s - %s - %s",
                    trilha.getOcupacao().getNome(),
                    trilha.getEmpresa().getNome(),
                    contador, LocalDate.now().getYear());
            trilha.setNome(nome);
            trilha.setApelido(trilha.getOcupacao().getNome()+contador);
            this.trilhaDao.create(trilha);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Trilha: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Trilha criada com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Trilha");
        if (id == null) {
            this.LOG.error("O ID da Trilha informada está nulo!");
            throw new RuntimeException("The ID is Null");
        }
        Trilha trilha = trilhaDao.getById(id);
        validaTrilhaNula(trilha);
        this.LOG.info("Trilha encontrada com sucesso!");
        if (this.trilhaDao.verificaSeTrilhaPossuiModulosAtivos(trilha.getId())) {
            this.LOG.error("A Trilha não pode ser excluida porque possui Módulos ativos relacionados!");
            throw new PersistenceException("The Trail cannot be excluded because it has active related Modules!");
        }

        getBeginTransaction();
        this.trilhaDao.delete(trilha);
        commitAndCloseTransaction();
        this.LOG.info("Trilha deletada com sucesso!");
    }

    public void update(Trilha novaTrilha, Long trilhaId) {
        this.LOG.info("Preparando para Atualizar a Trilha");
        if (novaTrilha == null || trilhaId == null){
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null");
        }
        Trilha trilha = this.trilhaDao.getById(trilhaId);
        validaTrilhaNula(trilha);
        this.LOG.info("Trilha encontrada com sucesso!");

        getBeginTransaction();
        trilha.setNivelDeSatisfacao(novaTrilha.getNivelDeSatisfacao());
        trilha.setAnotacoes(trilha.getAnotacoes());
        commitAndCloseTransaction();
        this.LOG.info("Trilha atualizada ocm sucesso!");
    }

    public List<Trilha> listAll() {
        this.LOG.info("Preparando para listar Trilhas");
        List<Trilha> trilhas = this.trilhaDao.listAll();
        if(trilhas == null) {
            this.LOG.info("Nenhuma Trilha foi encontrada!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+trilhas.size()+" Trilhas!");
        return trilhas;
    }

    public Trilha getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Trilha trilha = this.trilhaDao.getById(id);
        if(trilha == null) {
            this.LOG.error("Não foi encontrada a Trilha com ID " + id);
            throw new EntityNotFoundException("Trail not found!");
        }
        return trilha;
    }

    public List<Trilha> listByEmpresa(Long empresaId) {
        if(empresaId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        this.LOG.info("Preparando para buscar Trilhas da Empresa com ID "+empresaId);
        List<Trilha> trilhas = this.trilhaDao.listByEmpresa(empresaId);
        if(trilhas == null) {
            this.LOG.info("Nenhuma Trilha encontrada para Empresa com ID "+empresaId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+trilhas.size()+" Trilhas para Empresa com ID "+empresaId);
        return trilhas;
    }

    public List<Trilha> listByOcupacao(Long ocupacaoId) {
        if(ocupacaoId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        this.LOG.info("Preparando para buscar Trilhas da Ocupação de ID: "+ocupacaoId);
        List<Trilha> trilhas = this.trilhaDao.listByOcupacao(ocupacaoId);
        if(trilhas == null) {
            this.LOG.info("Nenhuma Trilha encontrada para Ocupação de ID: "+ocupacaoId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+trilhas.size()+" Trilhas para a Ocupação de ID: "+ocupacaoId);
        return trilhas;
    }

    private long geraContador(Long empresaId, String nomeOcupacao) {
        List<Trilha> trilhas = trilhaDao.listByEmpresa(empresaId);
        if (trilhas == null) return 1;
        return trilhas.stream()
                .filter(t -> t.getOcupacao().getNome().equals(nomeOcupacao))
                .count() + 1;
    }

    private void validaTrilhaNula(Trilha trilha) {
        if (trilha == null) {
            this.LOG.error("A Trilha não Existe!");
            throw new EntityNotFoundException("Trail not found!");
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
