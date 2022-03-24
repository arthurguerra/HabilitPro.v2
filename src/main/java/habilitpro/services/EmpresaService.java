package habilitpro.services;

import habilitpro.model.dao.EmpresaDao;
import habilitpro.model.persistence.Empresa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaService {

    private final Logger LOG = LogManager.getLogger(EmpresaService.class);

    private EntityManager entityManager;

    private EmpresaDao empresaDao;

    public EmpresaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.empresaDao = new EmpresaDao(entityManager);
    }

    public void create(Empresa empresa) {
        this.LOG.info("Preparando para a Criação de uma Empresa");
        validaEmpresaNula(empresa);

        if (!validaCNPJ(empresa.getCnpj())) {
            this.LOG.error("CNPJ Inválido!");
            throw new IllegalArgumentException("Invalid CNPJ!");
        }
        try {
            getBeginTransaction();
            this.empresaDao.create(empresa);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Empresa: " + e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Empresa criada com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar a Empresa");
        if (id == null) {
            this.LOG.error("O ID da Empresa informada está nulo!");
            throw new RuntimeException("The ID is Null");
        }
        Empresa empresa = this.empresaDao.getById(id);
        validaEmpresaNula(empresa);
        this.LOG.info("Empresa encontrada com sucesso!");

        try{
            getBeginTransaction();
            this.empresaDao.delete(empresa);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao deletar Empresa: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Empresa deletada com sucesso!");
    }

    public void update(Empresa novaEmpresa, Long empresaId) {
        this.LOG.info("Preparando para atualizar Empresa");
        if (novaEmpresa == null || empresaId == null) {
            this.LOG.error("Um dos parâmetros está nulo!");
            throw new RuntimeException("The parameter is null!");
        }
        Empresa empresa = this.empresaDao.getById(empresaId);
        validaEmpresaNula(empresa);
        this.LOG.info("Empresa encontrada no banco");

        getBeginTransaction();
        empresa.setNome(novaEmpresa.getNome());
        empresa.setCnpj(novaEmpresa.getCnpj());
        empresa.setEhFilial(novaEmpresa.isEhFilial());
        empresa.setNomeFilial(novaEmpresa.getNomeFilial());
        empresa.setSegmento(novaEmpresa.getSegmento());
        empresa.setCidade(novaEmpresa.getCidade());
        empresa.setEstado(novaEmpresa.getEstado());
        empresa.setRegional(novaEmpresa.getRegional());

        commitAndCloseTransaction();
        this.LOG.info("Empresa atualizada com sucesso!");
    }

    public List<Empresa> listAll() {
        this.LOG.info("Preparando para listar Empresas");
        List<Empresa> empresas = this.empresaDao.listAll();

        if (empresas == null) {
            this.LOG.info("Não foram encontradas Empresas");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ empresas.size() + " Empresas.");
        return empresas;
    }

    public List<Empresa> listByName(String nome) {
        if(nome == null || nome.isEmpty()) {
            this.LOG.info("O parâmetro nome está vazio!");
            throw new RuntimeException("The parameter name is null");
        }
        this.LOG.info("Preparando para buscar Empresas com o nome: " +nome);
        List<Empresa> empresas = this.empresaDao.listByName(nome);

        if (empresas == null) {
            this.LOG.info("Nenhuma Empresa foi encontrada!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ empresas.size() + " Empresas com o nome "+nome);
        return empresas;
    }

    private void validaEmpresaNula(Empresa empresa) {
        if (empresa == null) {
            this.LOG.error("A Empresa não Existe!");
            throw new EntityNotFoundException("Company not found!");
        }
    }

    private void getBeginTransaction() {
        this.LOG.info("Abrindo Transação com o banco de dados");
        entityManager.getTransaction().begin();
    }

    private void commitAndCloseTransaction() {
        this.LOG.info("Commitando e Fechando transação com o banco de dados");
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private boolean validaCNPJ(String CNPJ) {
        if (CNPJ.length() != 14) {
            return false;
        }

        int peso, soma, num, resultado;
        char digitoVerificador1, digitoVerificador2;

        try {
            peso = 2;
            soma = 0;
            for (int i = 11; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                soma += (num * peso);
                peso++;
                if (peso == 10) peso = 2;
            }
            resultado = soma % 11;

            if(resultado == 0 || resultado == 1) {
                digitoVerificador1 = '0';
            } else {
                digitoVerificador1 = (char) ((11 - resultado) + 48);
            }

            peso = 2;
            soma = 0;
            for (int i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                soma += (num * peso);
                peso++;
                if (peso == 10) peso = 2;
            }
            resultado = soma % 11;

            if(resultado == 0 || resultado == 1) {
                digitoVerificador2 = '0';
            } else {
                digitoVerificador2 = (char) ((11 - resultado) + 48);
            }

            return digitoVerificador1 == CNPJ.charAt(12) && digitoVerificador2 == CNPJ.charAt(13);

        } catch (Exception e) {
            System.err.println("CNPJ inválido!");
            return false;
        }
    }
}
