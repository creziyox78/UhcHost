package fr.lastril.uhchost.inventory.guis.rules.see.items;

import fr.lastril.uhchost.game.rules.world.LootsRules;
import fr.lastril.uhchost.inventory.guis.rules.see.RulesGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Arrays;

public class RulesLootsGui extends IQuickInventory {

    private ItemsCreator ic;

    public RulesLootsGui() {
        super(I18n.tl("guis.loots.name"), 9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("rulesloot", taskUpdate -> {
            ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.loots.apple"), Arrays.asList(I18n.tl("guis.loots.appleLore", String.valueOf(LootsRules.getInstance().getLoot(Material.APPLE)))));
            inv.setItem(ic.create(), 0);
            ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.loots.feather"), Arrays.asList(I18n.tl("guis.loots.featherLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FEATHER)))));
            inv.setItem(ic.create(), 1);
            ic = new ItemsCreator(Material.FLINT, I18n.tl("guis.loots.flint"), Arrays.asList(I18n.tl("guis.loots.flintLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FLINT)))));
            inv.setItem(ic.create(), 2);
            ic = new ItemsCreator(Material.STRING, I18n.tl("guis.loots.string"), Arrays.asList(I18n.tl("guis.loots.stringLore", String.valueOf(LootsRules.getInstance().getLoot(Material.STRING)))));
            inv.setItem(ic.create(), 3);
            inv.addRetourItem(new RulesGui());
        });


    }
}
