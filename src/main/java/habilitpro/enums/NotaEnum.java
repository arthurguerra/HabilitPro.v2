package habilitpro.enums;

public enum NotaEnum {

    um(1, "Um"),
    dois(2, "Dois"),
    tres(3, "Três"),
    quatro(4, "Quatro"),
    cinco(5, "Cinco");

    private int id;
    private String descricao;

    NotaEnum(int id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public int getValue() {
        return id;
    }

    public String getDisplayName() {
        return descricao;
    }

    @Override
    public String toString() {
        return ""+id;
    }
}
