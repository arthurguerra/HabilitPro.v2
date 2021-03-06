package habilitpro.model.persistence;

import habilitpro.enums.StatusModuloEnum;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "trilha_id")
    private Trilha trilha;

    @Column(nullable = false)
    private String nome;

    private String habilidades;

    private String tarefaValidacao;

    @Column(nullable = false)
    private int prazoLimite;

    @Enumerated(EnumType.STRING)
    private StatusModuloEnum statusModulo;

    private OffsetDateTime inicio;

    private OffsetDateTime fim;

    @OneToMany(mappedBy = "modulo")
    private Set<Avaliacao> avaliacoes;

    public Modulo() {
    }

    public Modulo(Trilha trilha, String nome, String habilidades, String tarefaValidacao, int prazoLimite) {
        this.trilha = trilha;
        this.nome = nome;
        this.habilidades = habilidades;
        this.tarefaValidacao = tarefaValidacao;
        this.prazoLimite = prazoLimite;
        this.statusModulo = StatusModuloEnum.nao_iniciado;
    }

    public Modulo(Trilha trilha, String nome, int prazoLimite) {
        this.trilha = trilha;
        this.nome = nome;
        this.prazoLimite = prazoLimite;
        this.statusModulo = StatusModuloEnum.nao_iniciado;
    }

    public Modulo(Trilha trilha, String nome) {
        this.trilha = trilha;
        this.nome = nome;
        this.prazoLimite = 10;
        this.statusModulo = StatusModuloEnum.nao_iniciado;
    }

    public void iniciarModulo() {
        this.statusModulo = StatusModuloEnum.andamento;
        this.inicio = OffsetDateTime.now();
    }

    public void finalizarModulo() {
        this.statusModulo = StatusModuloEnum.fase_avaliacao;
        this.fim = OffsetDateTime.now();
    }

    public void verificaPrazoLimite() {
        if (fim == null) throw new IllegalStateException("Finalize o m??dulo primeiro para depois verificar o prazo limite para a entrega da avalia????o.");

        OffsetDateTime diaPrazoLimite = fim;
        for (int i = prazoLimite; i > 0; i--) {
            diaPrazoLimite = diaPrazoLimite.plusDays(1);
            if (diaPrazoLimite.getDayOfWeek() == DayOfWeek.SATURDAY || diaPrazoLimite.getDayOfWeek() == DayOfWeek.SUNDAY) {
                i++;
            }
        }
        if (OffsetDateTime.now().isAfter(diaPrazoLimite)) {
            this.statusModulo = StatusModuloEnum.avaliacao_finalizada;
        }

//        if (DAYS.between(fim, OffsetDateTime.now()) > prazoLimite) {
//            this.status = StatusModulo.avaliacao_finalizada;
//        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trilha getTrilha() {
        return trilha;
    }

    public void setTrilha(Trilha trilha) {
        this.trilha = trilha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getTarefaValidacao() {
        return tarefaValidacao;
    }

    public void setTarefaValidacao(String tarefaValidacao) {
        this.tarefaValidacao = tarefaValidacao;
    }

    public int getPrazoLimite() {
        return prazoLimite;
    }

    public void setPrazoLimite(int prazoLimite) {
        this.prazoLimite = prazoLimite;
    }

    public StatusModuloEnum getStatusModulo() {
        return statusModulo;
    }

    public void setStatusModulo(StatusModuloEnum statusModulo) {
        this.statusModulo = statusModulo;
    }

    public OffsetDateTime getInicio() {
        return inicio;
    }

    public void setInicio(OffsetDateTime inicio) {
        this.inicio = inicio;
    }

    public OffsetDateTime getFim() {
        return fim;
    }

    public void setFim(OffsetDateTime fim) {
        this.fim = fim;
    }

    @Override
    public String toString() {
        return "Modulo{" +
                "id=" + id +
                ", trilha=" + trilha.getNome() +
                ", nome='" + nome + '\'' +
                ", habilidades='" + habilidades + '\'' +
                ", tarefaValidacao='" + tarefaValidacao + '\'' +
                ", prazoLimite=" + prazoLimite +
                ", statusModulo=" + statusModulo.getDisplayName() +
                ", inicio=" + inicio +
                ", fim=" + fim +
                '}';
    }
}
