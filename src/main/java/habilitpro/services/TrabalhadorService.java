package habilitpro.services;

import habilitpro.model.dao.TrabalhadorDao;
import habilitpro.model.persistence.Funcao;
import habilitpro.model.persistence.Setor;
import habilitpro.model.persistence.Trabalhador;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class TrabalhadorService {

    private final Logger LOG = LogManager.getLogger(TrabalhadorService.class);

    private EntityManager entityManager;

    private TrabalhadorDao trabalhadorDao;

    private EmpresaService empresaService;

    private FuncaoService funcaoService;

    private SetorService setorService;

    public TrabalhadorService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.trabalhadorDao = new TrabalhadorDao(entityManager);
        this.empresaService = new EmpresaService(entityManager);
        this.funcaoService = new FuncaoService(entityManager);
        this.setorService = new SetorService(entityManager);
    }

    public void create(Trabalhador trabalhador) {
        this.LOG.info("Preparando para criar Trabalhador");
        validaTrabalhadorNulo(trabalhador);
        validaTrabalhador(trabalhador);

        Setor setor = buscaSetorExistente(trabalhador.getSetor());
        if (setor != null) {
            this.LOG.info("Setor "+setor.getNome()+" encontrado no banco!");
            trabalhador.setSetor(setor);
        }

        Funcao funcao = buscaFuncaoExistente(trabalhador.getFuncao());
        if (funcao != null) {
            this.LOG.info("Função "+funcao.getNome()+" encontrada no banco!");
            trabalhador.setFuncao(funcao);
        }
        try {
            getBeginTransaction();
            this.trabalhadorDao.create(trabalhador);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.info("Erro ao criar Trabalhador: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Trabalhador criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar o Trabalhador no banco de dados");
        if (id == null) {
            this.LOG.error("O ID do Trabalhador informado está nulo!");
            throw new RuntimeException("Employee ID is Null!");
        }
        Trabalhador trabalhador = this.trabalhadorDao.getById(id);
        validaTrabalhadorNulo(trabalhador);
        this.LOG.info("Trabalhador encontrado com sucesso!");
        if(trabalhadorDao.verificaSeTrabalhadorPossuiAvaliacoesAtivas(id)) {
            this.LOG.error("O Trabalhador não pode ser excluido porque possui Avaliações ativas relacionadas!");
            throw new PersistenceException("The Employee cannot be excluded because it has active related Assessments!");
        }
        try {
            getBeginTransaction();
            this.trabalhadorDao.delete(trabalhador);
            commitAndCloseTransaction();
        }catch (Exception e) {
            this.LOG.error("Erro ao deletar Trabalhador: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Trabalhador deletado com sucesso!");
    }

    public void update(Trabalhador novoTrabalhador, Long trabalhadorId) {
        this.LOG.info("Preparando para atualizar Trabalhador");
        if (novoTrabalhador == null || trabalhadorId == null) {
            this.LOG.error("Parâmetro está nulo!");
            throw new RuntimeException("Parameter is null!");
        }
        validaTrabalhador(novoTrabalhador);
        Setor setor = buscaSetorExistente(novoTrabalhador.getSetor());
        if (setor != null) {
            this.LOG.info("Setor "+setor.getNome()+" encontrado no banco!");
            novoTrabalhador.setSetor(setor);
        }
        Funcao funcao = buscaFuncaoExistente(novoTrabalhador.getFuncao());
        if (funcao != null) {
            this.LOG.info("Função "+funcao.getNome()+" encontrada no banco!");
            novoTrabalhador.setFuncao(funcao);
        }
        Trabalhador trabalhador = this.trabalhadorDao.getById(trabalhadorId);
        validaTrabalhadorNulo(trabalhador);
        getBeginTransaction();

        trabalhador.setNome(novoTrabalhador.getNome());
        trabalhador.setCpf(novoTrabalhador.getCpf());
        trabalhador.setEmpresa(novoTrabalhador.getEmpresa());
        trabalhador.setSetor(novoTrabalhador.getSetor());
        trabalhador.setFuncao(novoTrabalhador.getFuncao());

        commitAndCloseTransaction();
        this.LOG.info("Trabalhador atualizado com sucesso!");
    }

    public List<Trabalhador> listAll() {
        this.LOG.info("Preparando para listar Trabalhadores");
        List<Trabalhador> trabalhadores = this.trabalhadorDao.listAll();
        if (trabalhadores == null){
            this.LOG.info("Não foi encontrado nenhum Trabalhador");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+trabalhadores.size()+" Trabalhadores");
        return trabalhadores;
    }

    public Trabalhador getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("ID is null!");
        }
        Trabalhador trabalhador = this.trabalhadorDao.getById(id);
        validaTrabalhadorNulo(trabalhador);
        return trabalhador;
    }

    public List<Trabalhador> listByEmpresa(Long empresaId) {
        if(empresaId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("ID is null");
        }
        this.LOG.info("Preparando para buscar Trabalhadores da Empresa com ID "+empresaId);
        List<Trabalhador> trabalhadores = this.trabalhadorDao.listByEmpresa(empresaId);
        if (trabalhadores == null) {
            this.LOG.info("Nenhum Trabalhador encontrado para Empresa com ID "+empresaId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+trabalhadores.size()+" Trabalhadores Para a Empresa de ID "+empresaId);
        return trabalhadores;
    }

    public List<Trabalhador> listBySetor(Long setorId) {
        if (setorId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("Id is null!");
        }
        this.LOG.info("Preparando para buscar Trabalhadores do Setor com ID "+setorId);
        List<Trabalhador> trabalhadores = this.trabalhadorDao.listBySetor(setorId);
        if (trabalhadores == null) {
            this.LOG.info("Nenhum Trabalhador encontrado paro o Setor de ID "+setorId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+trabalhadores.size()+" Trabalhadores para o Setor de ID "+setorId);
        return trabalhadores;
    }

    public List<Trabalhador> listByFuncao(Long funcaoId) {
        if (funcaoId == null) {
            this.LOG.info("O parâmetro ID está vazio!");
            throw new RuntimeException("Id is null!");
        }
        this.LOG.info("Preparando para buscar Trabalhadores com Função de ID "+funcaoId);
        List<Trabalhador> trabalhadores = this.trabalhadorDao.listByFuncao(funcaoId);
        if (trabalhadores == null) {
            this.LOG.info("Nenhum Trabalhador encontrado paro a Função de ID "+funcaoId);
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+trabalhadores.size()+" Trabalhadores para a Função de ID "+funcaoId);
        return trabalhadores;
    }

    private Setor buscaSetorExistente(Setor setor) {
        this.LOG.info("Bucando se já existe o Setor: "+setor.getNome());
        return this.setorService.findByName(setor.getNome());
    }

    private Funcao buscaFuncaoExistente(Funcao funcao) {
        this.LOG.info("Buscando se já existe a Função: "+funcao.getNome());
        return this.funcaoService.findByName(funcao.getNome());
    }

    private void validaTrabalhadorNulo(Trabalhador trabalhador) {
        if (trabalhador == null) {
            this.LOG.error("O Trabalhador não existe!");
            throw new EntityNotFoundException("Employee not found!");
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

    private void validaTrabalhador(Trabalhador trabalhador) {
        if(!validaCPF(trabalhador.getCpf())){
            this.LOG.error("CPF Inválido!");
            throw new IllegalArgumentException("Invalid CPF");
        }
    }

    private boolean validaCPF(String CPF) {
        if (CPF.length() != 11) {
            return false;
        }

        int peso, soma, num, resultado;
        char digitoVerificador1, digitoVerificador2;

        try {
            peso = 10;
            soma = 0;
            for (int i = 0; i < 9; i++) {
                num = (int) (CPF.charAt(i) - 48);
                soma += (num * peso);
                peso--;
            }
            resultado = 11 - (soma % 11);

            if(resultado == 10 || resultado == 11) {
                digitoVerificador1 = '0';
            } else {
                digitoVerificador1 = (char) (resultado + 48);
            }

            peso = 11;
            soma = 0;
            for (int i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                soma += (num * peso);
                peso--;
            }
            resultado = 11 - (soma % 11);

            if(resultado == 10 || resultado == 11) {
                digitoVerificador2 = '0';
            } else {
                digitoVerificador2 = (char) (resultado + 48);
            }

            return digitoVerificador1 == CPF.charAt(9) && digitoVerificador2 == CPF.charAt(10);

        } catch (Exception e) {
            System.err.println("CPF inválido!");
            return false;
        }
    }
}
