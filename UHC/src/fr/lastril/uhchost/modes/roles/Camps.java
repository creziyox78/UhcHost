package fr.lastril.uhchost.modes.roles;

import org.bukkit.ChatColor;

public enum Camps {

    /*
     * LG
     */
    LOUP_GAROU(ChatColor.RED, "§cVictoire des Loups-Garous !"),
    VILLAGEOIS(ChatColor.GREEN, "§aVictoire des Villageois !"),
    NEUTRES(ChatColor.YELLOW, "§eVictoire des neutres !"),

    COUPLE(ChatColor.LIGHT_PURPLE, "§dVictoire du couple !"),

    ANGE(ChatColor.GOLD, "§6Victoire de l'Ange !"),
    LOUP_GAROU_BLANC(ChatColor.WHITE, "§fVictoire du Loup-Garou Blanc !"),
    LOUP_GAROU_SOLITAIRE(ChatColor.WHITE, "§fVictoire du Loup-Garou Solitaire !"),
    ASSASSIN(ChatColor.BLUE, "§9Victoire de l'Assassin"),
    TRUBLION(ChatColor.DARK_PURPLE, "§5Victoire du Trublion !"),
    IMITATEUR(ChatColor.DARK_AQUA, "§3Victoire de l'Imitateur !"),
    ZIZANIE(ChatColor.DARK_GREEN, "§3Victoire du rôle solo zizané !"),
    TRAPPEUR(ChatColor.DARK_BLUE, "§9Victoire du Trappeur !"),
    CHASSEUR_DE_PRIME(ChatColor.DARK_RED, "§4Victoire du Chasseur de Prime !"),

    /*
     * DEMON SLAYER
     */

    SLAYER(ChatColor.GREEN, "§aVictoire des Slayers"),
    DEMONS(ChatColor.RED, "§cVictoire des Démons"),

    /*
     * BLEACH
     */

    SHINIGAMIS(ChatColor.BLUE, "§6Victoire des Shinigamis"),
    ARRANCARS(ChatColor.RED, "§cVictoire des Accancars"),

    /*
     * NARUTO
     */
    SHINOBI(ChatColor.GREEN, "§aVictoire des Shinobis !"),
    AKATSUKI(ChatColor.RED, "§cVictoire de l'Akatsuki !"),

    OROCHIMARU(ChatColor.DARK_PURPLE,"§aVictoire des membres d'Orochimaru !"),

    /*
     * NARUTO V2
     */
    TAKA(ChatColor.GOLD,"§6Victoire de Taka !"),
    JUBI(ChatColor.LIGHT_PURPLE, "§aVictoire de Jûbi !"),
    DANZO(ChatColor.GRAY, "§aVictoire de Danzo !"),
    GAARA(ChatColor.YELLOW, "§aVictoire de Gaara !"),

    /*
     * Naruto V2.5
     */

    ZABUZA_HAKU(ChatColor.AQUA, "§aVictoire de Haku et Zabuza !"),



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
