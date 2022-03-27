package habilitpro.services;

import habilitpro.model.dao.SetorDao;
import habilitpro.model.persistence.Setor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class SetorService {

    private final Logger LOG = LogManager.getLogger(SetorService.class);

    private EntityManager entityManager;

    private SetorDao setorDao;

    public SetorService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.setorDao = new SetorDao(entityManager);
    }

    public void create(Setor setor) {
        this.LOG.info("Preparando para criar Setor");
        if (setor == null) {
            this.LOG.error("Setor informado está nulo!");
            throw new RuntimeException("Department is null!");
        }
        try {
            getBeginTransaction();
            this.setorDao.create(setor);
            commitAndCloseTransaction();
        }catch (Exception e) {
            this.LOG.error("Erro ao criar Setor: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Setor foi criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar Setor");
        if (id == null) {
            this.LOG.error("O ID do Setor informado está nulo!");
            throw new RuntimeException("The ID is null!");
        }
        Setor setor = this.setorDao.getById(id);
        validaSetorNulo(setor);
        this.LOG.info("Setor encontrado com sucesso!");
        if (setorDao.verificaSeSetorPossuiTrabalhadoresAtivos(id)) {
            this.LOG.error("O Setor não pode ser excluido porque possui Trabalhadores ativos relacionadados!");
            throw new PersistenceException("The Department cannot be excluded because it has active related Employees!");
        }

        getBeginTransaction();
        this.setorDao.delete(setor);
        commitAndCloseTransaction();
        this.LOG.info("Setor deletado com sucesso!");
    }

    public void update(Setor novoSetor, Long setorId) {
        this.LOG.info("Preparando para atualizar Setor");
        if (novoSetor == null || setorId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null");
        }
        Setor setor = this.setorDao.getById(setorId);
        validaSetorNulo(setor);
        this.LOG.info("Setor encontrado com sucesso!");

        getBeginTransaction();
        setor.setNome(novoSetor.getNome());
        commitAndCloseTransaction();
        this.LOG.info("Setor atualizado com sucesso!");
    }

    public List<Setor> listAll() {
        this.LOG.info("Preparando para listar os Setores");
        List<Setor> setores = this.setorDao.listAll();

        if(setores == null) {
            this.LOG.info("Não foi encontrada nenhum Setor!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrada(s) "+ setores.size() + " Setores");
        return setores;
    }

    public Setor getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Setor setor = this.setorDao.getById(id);
        if(setor == null) {
            this.LOG.error("Não foi encontrada o Setor de id " + id);
            throw new EntityNotFoundException("Department not found!");
        }
        return setor;
    }

    public Setor findByName(String nome) {
        if (nome == null || nome.isEmpty()) {
            this.LOG.error("O Nome não pode ser Nulo!");
            throw new RuntimeException("Name is null!");
        }
        try {
            return this.setorDao.findByName(nome);
        } catch (NoResultException e) {
            this.LOG.info("Setor não encontrado! Criando novo Setor");
            return null;
        }
    }

    private void validaSetorNulo(Setor setor) {
        if (setor == null) {
            this.LOG.error("O Setor não Existe!");
            throw new EntityNotFoundException("Department not found!");
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
