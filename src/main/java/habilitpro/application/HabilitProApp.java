package habilitpro.application;

import habilitpro.connection.JpaConnectionFactory;
import habilitpro.enums.NotaEnum;
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
//        usuarioService.autenticaUsuarioESenha(getUsuario(), getSenha());



//        Usuario usuario = new Usuario("Administrador", "89086607047", "adm@teste.com", "12345678a");
//        Perfil perfil = new Perfil(usuario, PerfilAcessoEnum.administrativo);
//        usuario.addPerfil(perfil);
//        usuarioService.create(usuario);

//        Empresa empresa = new Empresa(
//                "SERVICO NACIONAL DE APRENDIZAGEM INDUSTRIAL",
//                "05867982000137",
//                true,
//                "Instituto SENAI de Tecnologia em Calçado e Logística",
//                SegmentoEnum.tic,
//                "Novo Hamburgo",
//                "Rio Grande do Sul",
//                RegionalSenaiEnum.sul);

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
