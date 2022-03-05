package fr.lastril.uhchost.inventory.guis.loots;

import fr.lastril.uhchost.game.rules.world.LootsRules;
import fr.lastril.uhchost.inventory.guis.rules.see.RulesGui;
import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Collections;

public class LootsGui extends IQuickInventory {
    public LootsGui() {
        super(I18n.tl("guis.loots.name"), 9);

    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("loot", taskUpdate -> {
            ItemsCreator ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.loots.apple"), Collections.singletonList(I18n.tl("guis.loots.appleLore", String.valueOf(LootsRules.getInstance().getLoot(Material.APPLE)))));
            inv.setItem(ic.create(), onClick -> {
                new AppleLootGui().open(onClick.getPlayer());
            }, 0);
            ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.loots.feather"), Collections.singletonList(I18n.tl("guis.loots.featherLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FEATHER)))));
            inv.setItem(ic.create(), onClick -> {
                new FeatherLootGui().open(onClick.getPlayer());
            }, 1);
            ic = new ItemsCreator(Material.FLINT, I18n.tl("guis.loots.flint"), Collections.singletonList(I18n.tl("guis.loots.flintLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FLINT)))));
            inv.setItem(ic.create(), onClick -> {
                new FlintLootGui().open(onClick.getPlayer());
            }, 2);
            ic = new ItemsCreator(Material.STRING, I18n.tl("guis.loots.string"), Collections.singletonList(I18n.tl("guis.loots.stringLore", String.valueOf(LootsRules.getInstance().getLoot(Material.STRING)))));
            inv.setItem(ic.create(), onClick -> {
                new StringLootGui().open(onClick.getPlayer());
            }, 3);
            inv.addRetourItem(new RulesGuiHost());
        });

    }
}