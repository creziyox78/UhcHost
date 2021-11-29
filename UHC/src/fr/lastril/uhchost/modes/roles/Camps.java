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
    IMITATEUR(ChatColor.DARK_AQUA),


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
