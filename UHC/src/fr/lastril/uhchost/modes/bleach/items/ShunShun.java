package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class ShunShun extends QuickItem{
    public ShunShun(UhcHost main) {
        super(Material.BRICK);
        super.setName("§cShun Shun");
        super.setLore("§dRégénère §a1 §dcoeur l'utilisateur",
                "§7lorqu'un joueur est frappé avec.",
                "§7",
                "§cToutes les 2 utilisations, fait perdre",
                "§c0.5 coeur à l'utilisateur.",
                "§7");
        super.glow(true);
    }
}
