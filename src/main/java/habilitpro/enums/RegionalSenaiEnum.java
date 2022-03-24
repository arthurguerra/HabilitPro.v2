package habilitpro.enums;

public enum RegionalSenaiEnum implements StringValueEnum{
    norte_nordeste("norte_nordeste", "Norte - Nordeste"),
    oeste("oeste", "Oeste"),
    sudeste("sudeste", "Sudeste"),
    centro_norte("centro_norte", "Centro - Norte"),
    vale_itajai("vale_itajai", "Vale do Itajaí"),
    vale_itapocu("vale_itapocu", "Vale do Itapocu"),
    litoral_sul("litoral_sul", "Litoral Sul"),
    alto_uruguai("alto_uruguai", "Alto Uruguai Catarinense"),
    vale_itajai_mirim("vale_itajai_mirim", "Vale do Itajaí Mirim"),
    centro_oeste("centro_oeste", "Centro - Oeste"),
    planalto_norte("planalto_norte", "Planalto Norte"),
    foz_rio_itajai("foz_rio_itajai", "Foz do Rio Itajaí"),
    sul("sul", "Sul"),
    serra("serra", "Serra Catarinense"),
    extremo_oeste("extremo_oeste", "Extremo Oeste"),
    alto_vale_itajai("alto_vale_itajai", "Alto Vale do Itajaí");

    private String idRegional;
    private String descricaoRegional;

    RegionalSenaiEnum(String idRegional, String descricaoRegional) {
        this.idRegional = idRegional;
        this.descricaoRegional = descricaoRegional;
    }

    @Override
    public String getValue() {
        return idRegional;
    }

    @Override
    public String getDisplayName() {
        return descricaoRegional;
    }
}
