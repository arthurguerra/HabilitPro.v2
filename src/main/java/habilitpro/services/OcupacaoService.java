package habilitpro.services;

import habilitpro.model.dao.OcupacaoDao;
import habilitpro.model.persistence.Ocupacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class OcupacaoService {

    private final Logger LOG = LogManager.getLogger(OcupacaoService.class);

    private EntityManager entityManager;

    private OcupacaoDao ocupacaoDao;

    public OcupacaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.ocupacaoDao = new OcupacaoDao(entityManager);
    }

    public void create(Ocupacao ocupacao) {
        this.LOG.info("Preparando para criar Ocupação");
        if (ocupacao == null) {
            this.LOG.error("Ocupação informada está nulo!");
            throw new RuntimeException("Company is null!");
        }
        try {
            getBeginTransaction();
            this.ocupacaoDao.create(ocupacao);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Ocupação: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Ocupação foi criada com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Ocupação");
        if (id == null) {
            this.LOG.error("O ID da Ocupação informada está nulo!");
            throw new RuntimeException("The ID is Null");
        }

        Ocupacao ocupacao = this.ocupacaoDao.getById(id);
        validaOcupacaoNula(ocupacao);
        this.LOG.info("Ocupação encontrada com sucesso!");

        getBeginTransaction();
        this.ocupacaoDao.delete(ocupacao);
        commitAndCloseTransaction();
        this.LOG.info("Ocupação deletada com sucesso!");
    }

    public void update(Ocupacao novaOcupacao, Long ocupacaoId) {
        this.LOG.info("Preparando para Atualizar Ocupação");
        if (novaOcupacao == null || ocupacaoId == null){
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null");
        }
        Ocupacao ocupacao = this.ocupacaoDao.getById(ocupacaoId);
        validaOcupacaoNula(ocupacao);
        this.LOG.info("Ocupação encontrada com sucesso!");

        getBeginTransaction();
        ocupacao.setNome(novaOcupacao.getNome());
        commitAndCloseTransaction();
        this.LOG.info("Ocupação atualizada com sucesso!");
    }

    private Ocupacao findByName(String nome) {
        if (nome == null || nome.isEmpty()) {
            this.LOG.error("O Nome não pode ser Nulo!");
            throw new RuntimeException("Name is null!");
        }
        try {
            return this.ocupacaoDao.findByName(nome);
        } catch (NoResultException e) {
            this.LOG.info("Ocupação não encontrada! Criando nova Ocupação");
            return null;
        }
    }

    public List<Ocupacao> listAll() {
        this.LOG.info("Preparando para listar as Ocupações");
        List<Ocupacao> ocupacoes = this.ocupacaoDao.listAll();

        if(ocupacoes == null) {
            this.LOG.info("Não foi encontrada nenhuma Ocupação!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrada(s) "+ ocupacoes.size() + " Ocupações");
        return ocupacoes;
    }

    public Ocupacao getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Ocupacao ocupacao = this.ocupacaoDao.getById(id);
        if(ocupacao == null) {
            this.LOG.error("Não foi encontrada a Ocupação de id " + id);
            throw new EntityNotFoundException("Occupation not found!");
        }
        return ocupacao;
    }

    private void validaOcupacaoNula(Ocupacao ocupacao) {
        if (ocupacao == null) {
            this.LOG.error("A Ocupação não Existe!");
            throw new EntityNotFoundException("Ocupation not found!");
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
