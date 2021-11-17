package fr.lastril.uhchost.modes.roles;

import org.bukkit.ChatColor;

public enum Camps {

    /*
     * LG
     */
    LOUP_GAROU(ChatColor.RED),
    VILLAGEOIS(ChatColor.GREEN),
    NEUTRES(ChatColor.YELLOW),

    COUPLE(ChatColor.LIGHT_PURPLE),

    ANGE(ChatColor.GOLD),
    LOUP_GAROU_BLANC(ChatColor.WHITE),
    ASSASSIN(ChatColor.BLUE),
    TRUBLION(ChatColor.DARK_PURPLE),

    /*
     * NARUTO
     */
    SHINOBI(ChatColor.GREEN),
    AKATSUKI(ChatColor.RED),

    OROCHIMARU(ChatColor.DARK_PURPLE),

    MADARA(ChatColor.DARK_RED),
    SANBI(ChatColor.GRAY),

    SASUKE(ChatColor.DARK_BLUE),

    /*
     * NARUTO V2
     */
    TAKA(ChatColor.GOLD),
    JUBI(ChatColor.LIGHT_PURPLE),
    DANZO(ChatColor.GRAY),
    GAARA(ChatColor.YELLOW),

    /*
     * Naruto V2.5
     */

    ZABUZA_HAKU(ChatColor.AQUA),

    /*
     * NONE
     */
    EGALITE(ChatColor.GRAY),
    ;


    private final ChatColor compoColor;

    Camps(ChatColor compoColor) {
        this.compoColor = compoColor;
    }

    public ChatColor getCompoColor() {
        return this.compoColor;
    }

}
