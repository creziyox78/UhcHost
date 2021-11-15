package fr.lastril.uhchost.enums;

import fr.lastril.uhchost.tools.API.FormatTime;

public enum Messages {

    SEPARATION("❘"),

    /*
     * COMMANDS
     */
    NOT_FOR_YOU(error("Vous ne pouvez pas faire cette commande sur vous !")),
    NOT_INGAME("§cCe PlayerManager n'est pas en jeu !"),
    UNKNOW_PLAYER("§cCe PlayerManager est introuvable !"),
    ONLY_PLAYER("§cSeul les PlayerManagers peuvent exécuter cette commande !"),
    NOT_PERM("§cVous n'avez pas la permission d'exécuter cette commande !"),
    NOT_NOW("§cVous ne pouvez pas faire cette commande maintenant !"),
    COMMAND_DISABLED("§cCette commande est désactivée !"),
    COOLDOWNPREFIX("§9Cooldown§8 »§B "),

    CANT_USE_MORE_POWER(error("Vous ne pouvez plus utiliser votre pouvoir !")),
    UNVALID_NUMBER(error("Veuillez entrer un chiffre valide !")),
    TEAM_FULL(error("§cCette team est pleine !")),
    USED_POWER("§aVous avez utilisé votre pouvoir !"),
    CLICK_HERE("§a§l[CLIQUE-ICI]"),
    NOTHAVE_ROLE(error("Vous n'avez pas de rôle !")),

    /*
     * PREFIX
     */
    TEAMCRAFT_PREFIX("§2§lTeam Crafter §8»§a "),
    NARUTO_PREFIX("§6§lNaruto §8» §e"),
    LOUP_GAROU_PREFIX("§6LG UHC » §e"),
    PREFIX_WITH_ARROW("§6§lUHC §8» §a"),
    PREFIX_WITH_SEPARATION("§6§lUHC §8❘ §a"),
    PREFIX_SPEC_STAFF("§8§lUHC SPEC§8 » §7"),
    PREFIX("§6§lUHC");




    private final String string;

    Messages(String s) {
        this.string = s;
    }

    public String getMessage() {
        return string;
    }

    public static String use(String core) {
        return "§cUtilisation : "+core;
    }

    public static String error(String error) {
        return "§cErreur : "+error;
    }

    public static String not(String searched) {
        return error("Vous n'êtes pas "+searched);
    }

    public static String cooldown(int roleCooldown) {
        return error("Il vous reste "+ new FormatTime(roleCooldown));
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public static String getStringBoolean(boolean bool) {
        return bool ? "§aOui" : "§cNon";
    }
}
