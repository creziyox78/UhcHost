package fr.lastril.uhchost.inventory.guis.world.ores;

import fr.lastril.uhchost.game.rules.BlocksRule;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class OresPerChunkGui extends IQuickInventory {

    private final BlocksRule rule;
    private BannerCreator bc;

    public OresPerChunkGui(BlocksRule rule) {
        super(I18n.tl("guis.oresminy.name"), 9);
        this.rule = rule;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {


            bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);


            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;
            }, 0);


            bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;

            }, 1);
            bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;
            }, 2);
            bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);



            ItemsCreator ic = new ItemsCreator(rule.id, "§e" + this.rule.round,
                    Arrays.asList(""));
            inv.setItem(ic.create(), onClick -> {
                new OresActionGui(rule).open(onClick.getPlayer());
            },4);


            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;
            }, 6);
            bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;
            }, 7);
            bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = this.rule.round + Integer.parseInt(bannerName);
                if (value < 0)
                    return;
                this.rule.round = value;
            }, 8);

        });

    }
}
