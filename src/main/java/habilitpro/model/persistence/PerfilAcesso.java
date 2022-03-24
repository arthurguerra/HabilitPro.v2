package habilitpro.model.persistence;

import habilitpro.enums.PerfilAcessoEnum;

import javax.persistence.*;

@Entity
public class PerfilAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilAcessoEnum perfilAcesso;

    public PerfilAcesso() {
    }

    public PerfilAcesso(Usuario usuario, PerfilAcessoEnum perfilAcesso) {
        this.usuario = usuario;
        this.perfilAcesso = perfilAcesso;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PerfilAcessoEnum getPerfilAcesso() {
        return perfilAcesso;
    }

    public void setPerfilAcesso(PerfilAcessoEnum perfilAcesso) {
        this.perfilAcesso = perfilAcesso;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", perfilAcesso=" + perfilAcesso +
                '}';
    }
}
