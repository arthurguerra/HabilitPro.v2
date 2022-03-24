package habilitpro.model.persistence;

import habilitpro.enums.RegionalSenaiEnum;
import habilitpro.enums.SegmentoEnum;

import javax.persistence.*;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private boolean ehFilial;

    private String nomeFilial;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SegmentoEnum segmento;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RegionalSenaiEnum regional;

    public Empresa() {
    }

    public Empresa(String nome, String cnpj, boolean ehFilial, String nomeFilial, SegmentoEnum segmento, String cidade, String estado, RegionalSenaiEnum regional) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.ehFilial = ehFilial;
        if (ehFilial) {
            this.nomeFilial = nomeFilial;
        } else {
            this.nomeFilial = "";
        }
//        this.nomeFilial = nomeFilial;
        this.segmento = segmento;
        this.cidade = cidade;
        this.estado = estado;
        this.regional = regional;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public boolean isEhFilial() {
        return ehFilial;
    }

    public void setEhFilial(boolean ehFilial) {
        this.ehFilial = ehFilial;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public SegmentoEnum getSegmento() {
        return segmento;
    }

    public void setSegmento(SegmentoEnum segmento) {
        this.segmento = segmento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public RegionalSenaiEnum getRegional() {
        return regional;
    }

    public void setRegional(RegionalSenaiEnum regional) {
        this.regional = regional;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", ehFilial=" + ehFilial +
                ", nomeFilial='" + nomeFilial + '\'' +
                ", segmento=" + segmento +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", regional=" + regional.getDisplayName() +
                '}';
    }
}
