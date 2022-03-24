package habilitpro.enums;

public enum SegmentoEnum implements StringValueEnum{
    alimentos_bebidas("alimentos_bebidas", "Alimentos e Bebidas"),
    celulose_papel("celulose_papel", "Celulose e Papel"),
    construcao("construcao", "Construção"),
    equipamentos_eletricos("equipamentos_eletricos", "Equipamentos Elétricos"),
    farmacos("farmacos", "Fármacos e Equipamentos de Saúde"),
    fumo("fumo", "Fumo"),
    automotiva("automotiva", "Indústria Automotiva"),
    ceramica("ceramica", "Indústrica Cerâmica"),
    diversa("diversa", "Indústria Diversa"),
    extrativa("extrativa", "Indústria Extrativa"),
    grafica("grafica", "Indústria Gráfica"),
    madeira_moveis("madeira_moveis", "Madeira e Móveis"),
    maquinas_equipamentos("maquinas_equipamentos", "Máquinas e Equipamentos"),
    metalurgia("metalurgia", "Metalmecânica e Metalurgia"),
    oleo_gas_eletricidade("oleo_gas_eletricidade", "Óleo, Gás e Eletricidade"),
    plasticos("plasticos", "Produtos Químicos e Plásticos"),
    saneamento("saneamento", "Saneamento básico"),
    tic("tic", "TIC"),
    textil("textil", "Têxtil, Confecção, Couro e Calçados");

    private String idSegmento;
    private String descricaoSegmento;

    SegmentoEnum(String idSegmento, String descricaoSegmento) {
        this.idSegmento = idSegmento;
        this.descricaoSegmento = descricaoSegmento;
    }

    @Override
    public String getValue() {
        return idSegmento;
    }

    @Override
    public String getDisplayName() {
        return descricaoSegmento;
    }
}
