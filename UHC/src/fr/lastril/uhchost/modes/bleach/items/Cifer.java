package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class Cifer extends QuickItem {
    public Cifer(UhcHost main) {
        super(Material.FISHING_ROD);
        super.setName("§aCifer");
        super.setInfinityDurability();
        super.glow(true);
        super.setLore("§7Permet d'attirer un joueur",
                "§7sur l'utilisateur.",
                "§7(Cooldown - 2 minutes)");
    }
}
