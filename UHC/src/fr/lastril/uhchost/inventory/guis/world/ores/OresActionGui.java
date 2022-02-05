package fr.lastril.uhchost.inventory.guis.world.ores;

import fr.lastril.uhchost.game.rules.BlocksRule;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Arrays;

public class OresActionGui extends IQuickInventory {

    private final BlocksRule rule;

    public OresActionGui(BlocksRule rule) {
        super(I18n.tl("guis.oreactions.name"), 9*1);
        this.rule = rule;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("ores", taskUpdate -> {
            int index = 0;
            ItemsCreator ic = new ItemsCreator(rule.id, I18n.tl("guis.oreactions.perChunk", String.valueOf(rule.round)), Arrays.asList(I18n.tl("guis.oreactions.lore")));
            inv.setItem(ic.create(), onClick -> {
                new OresPerChunkGui(rule).open(onClick.getPlayer());
            },index++);
            ic = new ItemsCreator(Material.BEDROCK, I18n.tl("guis.oreactions.minY", String.valueOf(rule.minY)), Arrays.asList(I18n.tl("guis.oreactions.lore")));
            inv.setItem(ic.create(), onClick -> {
                new OresMinYGui(rule).open(onClick.getPlayer());
            },index++);
            ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.oreactions.maxY",String.valueOf(rule.maxY)), Arrays.asList(I18n.tl("guis.oreactions.lore")));
            inv.setItem(ic.create(), onClick -> {
                new OresMaxYGui(rule).open(onClick.getPlayer());
            },index++);
            ic = new ItemsCreator(Material.INK_SACK, I18n.tl("guis.oreactions.size", String.valueOf(rule.size)), Arrays.asList(I18n.tl("guis.oreactions.lore")), 1, (byte)15);
            inv.setItem(ic.create(), onClick -> {
                new OresSizeGui(rule).open(onClick.getPlayer());
            },index++);
            ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Arrays.asList(""));
            inv.setItem(ic.create(), onClick -> {
                new OresGui().open(onClick.getPlayer());
            },index++);
        });
    }
}
