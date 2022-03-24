package habilitpro.model.persistence;

import habilitpro.enums.NotaEnum;

import javax.persistence.*;

@Entity
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id")
    private Modulo modulo;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "trabalhador_id")
    private Trabalhador trabalhador;

    @Enumerated(EnumType.ORDINAL)
    private NotaEnum nota;

    private String anotacoes;

    public Avaliacao() {
    }

    public Avaliacao(Modulo modulo, Trabalhador trabalhador, NotaEnum nota, String anotacoes) {
        this.modulo = modulo;
        this.trabalhador = trabalhador;
        this.nota = nota;
        this.anotacoes = anotacoes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Trabalhador getTrabalhador() {
        return trabalhador;
    }

    public void setTrabalhador(Trabalhador trabalhador) {
        this.trabalhador = trabalhador;
    }

    public NotaEnum getNota() {
        return nota;
    }

    public void setNota(NotaEnum nota) {
        this.nota = nota;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", modulo=" + modulo +
                ", trabalhador=" + trabalhador +
                ", nota=" + nota +
                ", anotacoes='" + anotacoes + '\'' +
                '}';
    }
}
