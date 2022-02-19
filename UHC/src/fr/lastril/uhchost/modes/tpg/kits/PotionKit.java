package fr.lastril.uhchost.modes.tpg.kits;

import fr.lastril.uhchost.modes.tpg.KitTaupe;
import fr.lastril.uhchost.tools.API.items.PotionItem;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.potion.PotionType;

public class PotionKit extends KitTaupe {

    public PotionKit() {
        super("Potion");
        super.addItem(new PotionItem(PotionType.POISON, 1, true).toItemStack(1));
        super.addItem(new PotionItem(PotionType.INSTANT_DAMAGE, 1, true).toItemStack(1));
        super.addItem(new PotionItem(PotionType.SPEED, 1, true).toItemStack(1));
        super.addItem(new PotionItem(PotionType.INSTANT_HEAL, 2, true).toItemStack(1));
    }
}
