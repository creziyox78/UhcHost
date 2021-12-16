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
        ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.loots.apple"), Arrays.asList(I18n.tl("guis.loots.appleLore", String.valueOf(LootsRules.getInstance().getLoot(Material.APPLE)))));
        inv.addItem(ic.create());
        ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.loots.feather"), Arrays.asList(I18n.tl("guis.loots.featherLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FEATHER)))));
        inv.addItem(ic.create());
        ic = new ItemsCreator(Material.FLINT, I18n.tl("guis.loots.flint"), Arrays.asList(I18n.tl("guis.loots.flintLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FLINT)))));
        inv.addItem(ic.create());
        ic = new ItemsCreator(Material.STRING, I18n.tl("guis.loots.string"), Arrays.asList(I18n.tl("guis.loots.stringLore", String.valueOf(LootsRules.getInstance().getLoot(Material.STRING)))));
        inv.addItem(ic.create());
        inv.addRetourItem(new RulesGui());

    }
}
