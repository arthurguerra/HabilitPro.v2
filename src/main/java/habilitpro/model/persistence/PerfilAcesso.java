package habilitpro.model.persistence;

import habilitpro.enums.PerfilAcessoEnum;

import javax.persistence.*;

@Entity
public class PerfilAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name="usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PerfilAcessoEnum perfilAcessoEnum;

    public PerfilAcesso() {
    }

    public PerfilAcesso(Usuario usuario, PerfilAcessoEnum perfilAcessoEnum) {
        this.usuario = usuario;
        this.perfilAcessoEnum = perfilAcessoEnum;
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
        return perfilAcessoEnum;
    }

    public void setPerfilAcesso(PerfilAcessoEnum perfilAcesso) {
        this.perfilAcessoEnum = perfilAcesso;
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id=" + id +
                ", usuario=" + usuario.getNome() +
                ", perfilAcesso=" + perfilAcessoEnum.getValue() +
                '}';
    }
}
