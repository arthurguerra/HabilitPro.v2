package habilitpro.services;

import habilitpro.model.dao.UsuarioDao;
import habilitpro.model.persistence.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UsuarioService {

    private final Logger LOG = LogManager.getLogger(UsuarioService.class);

    private EntityManager entityManager;

    private UsuarioDao usuarioDao;

    public UsuarioService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.usuarioDao = new UsuarioDao(entityManager);
    }

    public void create(Usuario usuario) {
        this.LOG.info("Preparando para a Criação de um Usuário");
        validaUsuarioNulo(usuario);
        validaUsuario(usuario);
        try {
            getBeginTransaction();
            this.usuarioDao.create(usuario);
            commitAndCloseTransaction();
        } catch (Exception e) {
            this.LOG.error("Erro ao criar Usuário: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Usuário criado com sucesso!");
    }

    public void delete(Long id) {
        this.LOG.info("Preparando para buscar o Usuário no Banco de Dados");
        if (id == null) {
            this.LOG.error("O ID do Usuário informado está nulo!");
            throw new RuntimeException("The User ID is Null!");
        }
        Usuario usuario = this.usuarioDao.getById(id);
        validaUsuarioNulo(usuario);
        this.LOG.info("Usuário encontrado com sucesso!");
        if (this.usuarioDao.verificaSeUsuarioPossuiPerfisAtivos(usuario.getId())) {
            this.LOG.error("O Usuário não pode ser excluido porque possui Perfis de Acesso ativos relacionados!");
            throw new PersistenceException("User cannot be excluded because it has active related Access Profiles!");
        }
        try {
            getBeginTransaction();
            this.usuarioDao.delete(usuario);
            commitAndCloseTransaction();
        }catch (Exception e) {
            this.LOG.error("Erro ao deletar Usuário: "+e.getMessage());
            throw new RuntimeException(e);
        }
        this.LOG.info("Usuário deletado com sucesso!");
    }

    public void update(Usuario novoUsuario, Long usuarioId) {
        this.LOG.info("Preprando para atualizar Usuário");
        if (novoUsuario == null || usuarioId == null) {
            this.LOG.error("O parâmetro está nulo!");
            throw new RuntimeException("The parameter is null!");
        }
        validaUsuario(novoUsuario);
        Usuario usuario = this.usuarioDao.getById(usuarioId);
        validaUsuarioNulo(usuario);
        getBeginTransaction();

        usuario.setNome(novoUsuario.getNome());
        usuario.setCpf(novoUsuario.getCpf());
        usuario.setEmail(novoUsuario.getEmail());
        usuario.setSenha(novoUsuario.getSenha());
        usuario.setPerfisAcesso(novoUsuario.getPerfisAcesso());

        commitAndCloseTransaction();
        this.LOG.info("Usuário atualizado com sucesso!");
    }

    public List<Usuario> listAll() {
        this.LOG.info("Preparando para listar Usuários");
        List<Usuario> usuarios = this.usuarioDao.listAll();

        if (usuarios == null) {
            this.LOG.info("Não foram encontrados Usuários");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ usuarios.size() + " Usuários.");
        return usuarios;
    }

    public Usuario getById(Long id) {
        if (id == null) {
            this.LOG.error("O ID está nulo!");
            throw new RuntimeException("Id is null!");
        }
        Usuario usuario = this.usuarioDao.getById(id);
        validaUsuarioNulo(usuario);
        return usuario;
    }

    public List<Usuario> listByName(String name) {
        if(name == null || name.isEmpty()) {
            this.LOG.info("O parâmetro nome está vazio!");
            throw new RuntimeException("The parameter name is null!");
        }

        this.LOG.info("Preparando para buscar Usuários com o nome: " +name);
        List<Usuario> usuarios = this.usuarioDao.listByName(name);

        if (usuarios == null) {
            this.LOG.info("Não foram encontrados Usuários!");
            return new ArrayList<>();
        }
        this.LOG.info("Foram encontrados "+ usuarios.size() + " Usuários com o nome "+name);
        return usuarios;
    }

    public boolean autenticaUsuarioESenha(String email, String senha) {
        this.LOG.info("Preparando para autenticar usuário/senha");
        Usuario usuario;
        try {
            usuario = this.usuarioDao.findByEmail(email);
        } catch (NoResultException e) {
            this.LOG.error("Usuário não cadastrado!");
            throw new RuntimeException("User not found!");
        }
        if (!Objects.equals(senha, usuario.getSenha())) {
            this.LOG.error("Senha Incorreta!");
            throw new RuntimeException("Invalid Password!");
        }
        this.LOG.info("Autenticação de Usuário Finalizada com Sucesso!");
        return true;
    }

    private void validaUsuario(Usuario usuario) {
        if(!validaEmail(usuario.getEmail())) {
            this.LOG.error("Email Inválido! O e-mail deve estar no formato “usuario@dominio.terminacao”");
            throw new IllegalArgumentException("Invalid Email!");
        }
        if(!validaSenha(usuario.getSenha())) {
            this.LOG.error("Senha Inválida! A senha deve possuir, no mínimo, 8 caracteres, e deve conter letras e números.");
            throw new IllegalArgumentException("Invalid Password!");
        }
        if(!validaCPF(usuario.getCpf())) {
            this.LOG.error("CPF Inválido!");
            throw new IllegalArgumentException("Invalid CPF!");
        }
    }

    private void validaUsuarioNulo(Usuario usuario) {
        if (usuario == null) {
            this.LOG.error("Usuário não encontrado!");
            throw new RuntimeException("User not found!");
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

    private boolean validaEmail(String email) {
        String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{1,})$";
        return email.matches(emailRegex);
    }

    private boolean validaSenha(String senha) {
        String senhaRegex = "(?=^.{8,}$)((?=.*[a-zA-Z])(?=.*[0-9]))^.*";
        return senha.matches(senhaRegex);
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
