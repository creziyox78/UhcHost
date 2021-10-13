package fr.lastril.uhchost.enums;

public enum Messages {

    LOUP_GAROU_PREFIX("§6LG UHC » §e");

    private final String string;

    Messages(String s) {
        this.string = s;
    }

    public String getPrefix() {
        return string;
    }
}
