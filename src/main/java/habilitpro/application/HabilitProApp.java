package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.enums.NotaEnum;
import habilitpro.enums.PerfilAcessoEnum;
import habilitpro.model.persistence.*;
import habilitpro.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

public class HabilitProApp {

    private static final Logger LOG = LogManager.getLogger(HabilitProApp.class);

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
        UsuarioService usuarioService = new UsuarioService(entityManager);
        EmpresaService empresaService = new EmpresaService(entityManager);
        TrilhaService trilhaService = new TrilhaService(entityManager);
        OcupacaoService ocupacaoService = new OcupacaoService(entityManager);
        ModuloService moduloService = new ModuloService(entityManager);
        TrabalhadorService trabalhadorService = new TrabalhadorService(entityManager);
        AvaliacaoService avaliacaoService = new AvaliacaoService(entityManager);
        SetorService setorService = new SetorService(entityManager);
        FuncaoService funcaoService = new FuncaoService(entityManager);
        PerfilAcessoService perfilAcessoService = new PerfilAcessoService(entityManager);

//        usuarioService.autenticaUsuarioESenha(getUsuario(), getSenha());


    }

    private static String getUsuario() {
        System.out.print("Usuário: ");
        return sc.nextLine();
    }

    private static String getSenha() {
        System.out.print("Senha: ");
        return sc.nextLine();
    }
}
