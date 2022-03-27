package habilitpro.services;

import habilitpro.model.dao.FuncaoDao;
import habilitpro.model.persistence.Funcao;
import habilitpro.model.persistence.Ocupacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class FuncaoService {

    private final Logger LOG = LogManager.getLogger(FuncaoService.class);

    private EntityManager entityManager;

    private FuncaoDao funcaoDao;

    public FuncaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.funcaoDao = new FuncaoDao(entityManager);
    }

    public void create(Funcao funcao) {
        this.LOG.info("Preparando para criar Função");
        if (funcao == null) {
            this.LOG.error("A Função informada está nula!");
            throw new RuntimeException("Role is null!");
        }
        try {
            getBeginTransaction();
            this.funcaoDao.create(funcao);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Função: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Função criada com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Função");
        if (id == null) {
            this.LOG.error("O ID da Função informada está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Funcao funcao = this.funcaoDao.getById(id);
        validaFuncaoNula(funcao);
        this.LOG.info("Função encontrada com sucesso!");
        if (funcaoDao.verificaSeFuncaoPossuiTrabalhadoresAtivos(id)) {
            this.LOG.error("A Função não pode ser excluida porque possui Trabalhadores ativos relacionadados!");
            throw new PersistenceException("The Role cannot be excluded because it has active related Employees!");
        }

        getBeginTransaction();
        this.funcaoDao.delete(funcao);
        commitAndCloseTransaction();
        this.LOG.info("Função deletada com sucesso!");
    }

    public void update(Funcao novaFuncao, Long funcaoId) {
        this.LOG.info("Preparando para atualizar Função");
        if (novaFuncao == null || funcaoId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("Parameter is null!");
        }
        Funcao funcao = this.funcaoDao.getById(funcaoId);
        validaFuncaoNula(funcao);
        this.LOG.info("Função encontrada com sucesso!");

        getBeginTransaction();
        funcao.setNome(novaFuncao.getNome());
        commitAndCloseTransaction();
        this.LOG.info("Função atualizada com sucesso!");
    }

    public List<Funcao> listAll() {
        this.LOG.info("Preparando para listar Funções");
        List<Funcao> funcoes = this.funcaoDao.listAll();

        if (funcoes == null) {
            this.LOG.info("Nenhuma Função foi encontrada!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrada(s) "+funcoes.size()+" Funções");
        return funcoes;
    }

    public Funcao getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("Id is null!");
        }
        Funcao funcao = this.funcaoDao.getById(id);
        if (funcao == null) {
            this.LOG.error("Não foi encontrada a Função de ID "+id);
            throw new EntityNotFoundException("Role not found!");
        }
        return funcao;
    }

    public Funcao findByName(String nome) {
        if (nome == null || nome.isEmpty()) {
            this.LOG.error("O Nome não pode ser Nulo!");
            throw new RuntimeException("Name is null!");
        }
        try {
            return this.funcaoDao.findByName(nome);
        } catch (NoResultException e) {
            this.LOG.info("Função não encontrada! Criando nova Função");
            return null;
        }
    }

    private void validaFuncaoNula(Funcao funcao) {
        if (funcao == null) {
            this.LOG.error("A Função não Existe!");
            throw new EntityNotFoundException("Role not found!");
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

