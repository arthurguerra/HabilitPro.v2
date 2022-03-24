package habilitpro.enums;

public enum PerfilAcessoEnum implements StringValueEnum{
    administrativo("administrativo", "Administrativo (equipe SENAI) - Realizar cadastros gerais, parametrizações e emissão de relatórios"),
    operacional("operacional", "Operacional (Supervisor da empresa) - Realizar processo de avaliação e emissão de relatórios"),
    rh("rh", "RH (equipe RH) - Acompanhar processo de avaliação e emissão de relatórios");

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
