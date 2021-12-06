package fr.lastril.uhchost.modes.roles;

import org.bukkit.ChatColor;

public enum Camps {

    /*
     * LG
     */
    LOUP_GAROU(ChatColor.RED, "§cVictoire des Loups-Garous"),
    VILLAGEOIS(ChatColor.GREEN, "§aVictoire des Villageois"),
    NEUTRES(ChatColor.YELLOW, "§eVictoire des neutres !"),

    COUPLE(ChatColor.LIGHT_PURPLE, "§dVictoire du couple !"),

    ANGE(ChatColor.GOLD, "§6Victoire de l'Ange !"),
    LOUP_GAROU_BLANC(ChatColor.WHITE, "§fVictoire du Loup-Garou Blanc !"),
    ASSASSIN(ChatColor.BLUE, "§9Victoire de l'Assassin'"),
    TRUBLION(ChatColor.DARK_PURPLE, "§5Victoire du Trublion !"),
    IMITATEUR(ChatColor.DARK_AQUA, "§3Victoire de l'Imitateur !"),
    ZIZANIE(ChatColor.DARK_GREEN, "§3Voictoire du rôle solo zizané !"),


    /*
     * NONE
     */
    EGALITE(ChatColor.GRAY, "§7Personne n'a gagné la partie !"),
    ;


    private final ChatColor compoColor;
    private final String winMessage;

    Camps(ChatColor compoColor, String winMessage) {
        this.compoColor = compoColor;
        this.winMessage = winMessage;
    }

    public ChatColor getCompoColor() {
        return this.compoColor;
    }

    public String getWinMessage() {
        return winMessage;
    }
}
