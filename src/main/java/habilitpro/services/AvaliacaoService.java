package habilitpro.services;

import habilitpro.model.dao.AvaliacaoDao;
import habilitpro.model.persistence.Avaliacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AvaliacaoService {

    private final Logger LOG = LogManager.getLogger(AvaliacaoService.class);

    private EntityManager entityManager;

    private AvaliacaoDao avaliacaoDao;

    public AvaliacaoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.avaliacaoDao = new AvaliacaoDao(entityManager);
    }

    public void create(Avaliacao avaliacao) {
        this.LOG.info("Preparando para criar Avaliação");
        validaAvaliacaoNula(avaliacao);
        try {
            getBeginTransaction();
            this.avaliacaoDao.create(avaliacao);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.info("Erro ao criar Avaliação: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Avaliação criada com sucesso!");
    }

    public void delete(Long id){
        this.LOG.info("Preparando para buscar a Avaliação no banco de dados");
        if (id == null) {
            this.LOG.error("O ID da Avaliação informada está nulo!");
            throw new RuntimeException("Assessment ID is null!");
        }
        Avaliacao avaliacao = this.avaliacaoDao.getById(id);
        validaAvaliacaoNula(avaliacao);
        this.LOG.info("Avaliação encontrada com sucesso!");
        try {
            getBeginTransaction();
            this.avaliacaoDao.delete(avaliacao);
            commitAndCloseTransaction();
        }catch (Exception e){
            this.LOG.error("Erro ao deletar Avaliação: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Avaliação deletada com sucesso!");
    }

    public void update(Avaliacao novaAvaliacao, Long avaliacaoId) {
        this.LOG.info("Preparando para atualizar Avaliação");
        if (novaAvaliacao == null || avaliacaoId == null) {
            this.LOG.error("Parâmetro está nulo!");
            throw new RuntimeException("Parameter is null!");
        }
        Avaliacao avaliacao = this.avaliacaoDao.getById(avaliacaoId);
        validaAvaliacaoNula(avaliacao);

        getBeginTransaction();
        avaliacao.setNota(novaAvaliacao.getNota());
        avaliacao.setAnotacoes(novaAvaliacao.getAnotacoes());
        commitAndCloseTransaction();
        this.LOG.info("Avaliação atualizada com sucesso!");
    }

    public List<Avaliacao> listAll() {
        this.LOG.info("Preparando para listar Avaliações");
        List<Avaliacao> avaliacoes = this.avaliacaoDao.listAll();
        if (avaliacoes == null) {
            this.LOG.info("Nenhuma Avaliação encontrada!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+avaliacoes.size()+" Avaliações");
        return avaliacoes;
    }

    public Avaliacao getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Avaliacao avaliacao = this.avaliacaoDao.getById(id);
        validaAvaliacaoNula(avaliacao);
        return avaliacao;
    }

    public List<Avaliacao> listByModulo(Long moduloId) {
        if(moduloId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        this.LOG.info("Preparando para buscar Avaliações do Módulo com ID "+moduloId);
        List<Avaliacao> avaliacoes = this.avaliacaoDao.listByModulo(moduloId);
        if (avaliacoes == null) {
            this.LOG.info("Nenhuma Avaliação encontrada para o Módulo de ID "+moduloId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+avaliacoes.size()+" Avaliações para o Módulo de ID "+moduloId);
        return avaliacoes;
    }

    public List<Avaliacao> listByTrabalhador(Long trabalhadorId) {
        if(trabalhadorId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        this.LOG.info("Preparando para buscar Avaliações do Trabalhador com ID "+trabalhadorId);
        List<Avaliacao> avaliacoes = this.avaliacaoDao.listByTrabalhador(trabalhadorId);
        if (avaliacoes == null) {
            this.LOG.info("Nenhuma Avaliação encontrada para o Trabalhador de ID "+trabalhadorId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontradas "+avaliacoes.size()+" Avaliações para o Trabalhador de ID "+trabalhadorId);
        return avaliacoes;
    }

    private void validaAvaliacaoNula(Avaliacao avaliacao) {
        if (avaliacao == null) {
            this.LOG.error("A Avaliação não existe!");
            throw new EntityNotFoundException("Assessment not found!");
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
