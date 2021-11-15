package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.potion.Potion;

import java.util.Arrays;

public class RulesPotionsGui extends IQuickInventory {
    public RulesPotionsGui() {
        super(I18n.tl("guis.rulesPotions.name"), 54);
    }

    @Override
    public void contents(QuickInventory inv) {
        for (Potion potion : (UhcHost.getInstance()).gameManager.getDeniedPotions()) {
            inv.addItem(potion.toItemStack(1));
        }
        ItemsCreator ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Arrays.asList(""));
        inv.setItem(ic.create(), onClick -> {
            onClick.getPlayer().closeInventory();
        },53);
    }
}
