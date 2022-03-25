package habilitpro.model.persistence;

import javax.persistence.*;

@Entity
public class Funcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Funcao() {
    }

    public Funcao(String nome) {
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
        return "Funcao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
