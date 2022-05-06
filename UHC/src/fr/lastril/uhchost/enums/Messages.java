package fr.lastril.uhchost.enums;

import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.I18n;

public enum Messages {

    SEPARATION("❘"),

    /*
     * COMMANDS
     */
    NOT_FOR_YOU(error("Vous ne pouvez pas faire cette commande sur vous !")),
    NOT_INGAME("§cCe joueur n'est pas en jeu !"),
    UNKNOW_PLAYER("§cCe joueur est introuvable !"),
    ONLY_PLAYER("§cSeul les joueurs peuvent éxécuter cette commande !"),
    NOT_PERM(I18n.tl("no-permission")),
    NOT_NOW("§cVous ne pouvez pas faire cette commande maintenant !"),
    COMMAND_DISABLED("§cCette commande est désactivée !"),
    COOLDOWNPREFIX("§9Cooldown§8 »§B "),

    CANT_USE_POWER_NOW("§cVous ne pouvez pas utiliser votre pouvoir pour le moment."),
    CANT_USE_MORE_POWER(error("Vous ne pouvez plus utiliser votre pouvoir !")),
    UNVALID_NUMBER(error("Veuillez entrer un chiffre valide !")),
    TEAM_FULL(error("§cCette team est pleine !")),
    USED_POWER("§aExécution du pouvoir !"),
    CLICK_HERE("§e[CLIQUE-ICI]"),
    NOTHAVE_ROLE(error("Vous n'avez pas de rôle !")),

    /*
     * PREFIX
     */
    NARUTO_PREFIX("§e[§6Naruto§e] "),
    BLEACH_PREFIX("§9Bleach §f»§b "),
    LOUP_GAROU_PREFIX("§b[§6LG UHC§b] "),
    SLAVE_MARKET_PREFIX("§b§lSlave Market §f❘§9 "),
    TAUPE_GUN_PREFIX("§e§lTaupe Gun » "),
    PREFIX_WITH_ARROW("§4[§cUHC§4] §e"),
    PREFIX_WITH_SEPARATION("§6§lUHC §8❘ §a"),
    PREFIX_SPEC_STAFF("§8§lUHC STAFF§8 » §f"),
    SPECTATOR_PREFIX("§8[§4Spectateur§8] §7"),
    PREFIX("§6§lUHC"),
    ALREADY_USED_POWER("§cVous avez déjà utilisé ce pouvoir !"),
    ;




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
        return error("Vous devez patienter encore "+ new FormatTime(roleCooldown).toFormatString() + " avant de réutiliser votre pouvoir.");
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public static String getStringBoolean(boolean bool) {
        return bool ? "§aOui" : "§cNon";
    }
}
