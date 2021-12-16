package fr.lastril.uhchost.inventory.guis.timer;

import fr.lastril.uhchost.game.rules.world.LootsRules;
import fr.lastril.uhchost.inventory.guis.loots.AppleLootGui;
import fr.lastril.uhchost.inventory.guis.loots.FeatherLootGui;
import fr.lastril.uhchost.inventory.guis.loots.FlintLootGui;
import fr.lastril.uhchost.inventory.guis.loots.StringLootGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import org.bukkit.Material;

import java.util.Collections;

public class LootsGui extends IQuickInventory {
    public LootsGui() {
        super(I18n.tl("guis.loots.name"), 9);

    }

    @Override
    public void contents(QuickInventory inv) {
        ItemsCreator ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.loots.apple"), Collections.singletonList(I18n.tl("guis.loots.appleLore", String.valueOf(LootsRules.getInstance().getLoot(Material.APPLE)))));
        inv.addItem(ic.create(), onClick -> {
            new AppleLootGui().open(onClick.getPlayer());
        });
        ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.loots.feather"), Collections.singletonList(I18n.tl("guis.loots.featherLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FEATHER)))));
        inv.addItem(ic.create(), onClick -> {
            new FeatherLootGui().open(onClick.getPlayer());
        });
        ic = new ItemsCreator(Material.FLINT, I18n.tl("guis.loots.flint"), Collections.singletonList(I18n.tl("guis.loots.flintLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FLINT)))));
        inv.addItem(ic.create(), onClick -> {
            new FlintLootGui().open(onClick.getPlayer());
        });
        ic = new ItemsCreator(Material.STRING, I18n.tl("guis.loots.string"), Collections.singletonList(I18n.tl("guis.loots.stringLore", String.valueOf(LootsRules.getInstance().getLoot(Material.STRING)))));
        inv.addItem(ic.create(), onClick -> {
            new StringLootGui().open(onClick.getPlayer());
        });
        ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null);
        inv.setItem(ic.create(), 8);
    }
}