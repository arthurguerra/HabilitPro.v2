package habilitpro.enums;

public enum PerfilAcessoEnum implements StringValueEnum{
    administrativo("administrativo", "Administrativo"),
    operacional("operacional", "Operacional"),
    rh("rh", "Recursos Humanos");

    private String idPerfil;
    private String descricaoPerfil;

    PerfilAcessoEnum(String idPerfil, String descricaoPerfil) {
        this.idPerfil = idPerfil;
        this.descricaoPerfil = descricaoPerfil;
    }

    @Override
    public String getValue() {
        return idPerfil;
    }

    @Override
    public String getDisplayName() {
        return descricaoPerfil;
    }
}
