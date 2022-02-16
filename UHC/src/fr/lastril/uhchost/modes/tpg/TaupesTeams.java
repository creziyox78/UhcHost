package fr.lastril.uhchost.modes.tpg;

public enum TaupesTeams {

    TAUPE_1("§cT1 "),
    TAUPE_2("§cT2 "),
    TAUPE_3("§cT3 "),
    TAUPE_4("§cT4 "),
    TAUPE_5("§cT5 "),
    TAUPE_6("§cT6 "),
    TAUPE_7("§cT7 "),
    ;

    private final String prefix;

    TaupesTeams(String prefix){
        this.prefix =prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
