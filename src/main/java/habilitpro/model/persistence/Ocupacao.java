package habilitpro.model.persistence;

import javax.persistence.*;

@Entity
public class Ocupacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Ocupacao() {
    }

    public Ocupacao(String nome) {
        this.nome = nome;
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

    @Override
    public String toString() {
        return "Ocupacao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
