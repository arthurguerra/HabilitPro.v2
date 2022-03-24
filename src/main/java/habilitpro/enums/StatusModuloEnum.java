package habilitpro.enums;

public enum StatusModuloEnum implements StringValueEnum{
    nao_iniciado("nao_iniciado", "Curso não iniciado"),
    andamento("andamento", "Curso em andamento"),
    fase_avaliacao("fase_avaliacao", "Em fase de avaliação"),
    avaliacao_finalizada("avaliacao_finalizada", "Fase de avaliação finalizada");

    private String idStatus;
    private String descricaoStatus;

    StatusModuloEnum(String idStatus, String descricaoStatus) {
        this.idStatus = idStatus;
        this.descricaoStatus = descricaoStatus;
    }

    @Override
    public String getValue() {
        return this.idStatus;
    }

    @Override
    public String getDisplayName() {
        return this.descricaoStatus;
    }
}
