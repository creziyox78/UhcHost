package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class GetsugaTensho extends QuickItem {
    public GetsugaTensho(UhcHost main) {
        super(Material.GOLD_HOE);
        super.glow(true);
        super.setName("§eGetsuga Tensho");
        super.setLore("",
                "§fUtilisation : Chargement de 2 secondes (clique droit)",
                "§7Envoie une attaque (5x7).",
                "§7Les joueurs touchés sont expulsés",
                "§7et perdent§c 2 coeurs§7.",
                "§7(Cooldown - 2 minutes 30 secondes)");
        super.setInfinityDurability();
    }
}
