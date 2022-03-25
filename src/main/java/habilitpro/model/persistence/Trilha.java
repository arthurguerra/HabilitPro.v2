package habilitpro.model.persistence;

import habilitpro.enums.NotaEnum;

import javax.persistence.*;

@Entity
public class Trilha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ocupacao_id")
    private Ocupacao ocupacao;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String apelido;

    @Enumerated(EnumType.ORDINAL)
    private NotaEnum nivelDeSatisfacao;

    private String anotacoes;

    public Trilha() {
    }

    public Trilha(Empresa empresa, Ocupacao ocupacao) {
        this.empresa = empresa;
        this.ocupacao = ocupacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Ocupacao getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(Ocupacao ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public NotaEnum getNivelDeSatisfacao() {
        return nivelDeSatisfacao;
    }

    public void setNivelDeSatisfacao(NotaEnum nivelDeSatisfacao) {
        this.nivelDeSatisfacao = nivelDeSatisfacao;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    @Override
    public String toString() {
        return "Trilha{" +
                "id=" + id +
                ", empresa=" + empresa.getNome() +
                ", ocupacao=" + ocupacao.getNome() +
                ", nome='" + nome + '\'' +
                ", apelido='" + apelido + '\'' +
                ", nivelDeSatisfacao=" + nivelDeSatisfacao +
                ", anotacoes='" + anotacoes + '\'' +
                '}';
    }
}
