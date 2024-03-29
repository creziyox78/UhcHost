package fr.lastril.uhchost.inventory.guis.loots;

import fr.lastril.uhchost.game.rules.world.LootsRules;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Collections;

public class FeatherLootGui extends IQuickInventory {
    private BannerCreator bc;
    private ItemsCreator ic;
    public FeatherLootGui() {
        super(I18n.tl("guis.featherLoot.name"), 1*9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);

            }, 0);
            bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);

            }, 1);
            bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);
            }, 2);
            bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);


            ic = new ItemsCreator(Material.FEATHER, "§a"+ LootsRules.getInstance().getLoot(Material.FEATHER) + "%", Collections.singletonList(I18n.tl("guis.featherLoot.lore")));
            inv.setItem(ic.create(), onClick-> new LootsGui().open(onClick.getPlayer()),4);

            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);
            }, 6);
            bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);
            }, 7);
            bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = LootsRules.getInstance().getLoot(Material.FEATHER) + Integer.parseInt(bannerName);
                LootsRules.getInstance().setLoot(Material.FEATHER, value);
            }, 8);
        });
    }
}
