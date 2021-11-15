package fr.lastril.uhchost.modes.roles;

public enum But {

    /*
     * LG
     */
    LOUPS_GAROUS("Votre but est de tuer les Loups-Garous"),
    VILLAGEOIS("Votre but est de tuer les Villageois"),
    COUPLE("Votre but est de gagner en Couple."),

    WITH_TEAM("Votre but est d'empêcher les autres camps d'accomplir leur objectifs."),
    SOLO("Votre but est de gagner Seul.");

    public String desc;

    But(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc + ".";
    }

}
