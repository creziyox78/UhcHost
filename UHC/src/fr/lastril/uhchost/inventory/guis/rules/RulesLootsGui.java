package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.game.LootsRules;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RulesLootsGui extends Gui {
    public RulesLootsGui(Player player) {
        super(player, 9, I18n.tl("guis.loots.name"));
        ItemsCreator ic = new ItemsCreator(Material.APPLE, I18n.tl("guis.loots.apple"), Arrays.asList(I18n.tl("guis.loots.appleLore", String.valueOf(LootsRules.getInstance().getLoot(Material.APPLE)))));
        inventory.addItem(ic.create());
        ic = new ItemsCreator(Material.FEATHER, I18n.tl("guis.loots.feather"), Arrays.asList(I18n.tl("guis.loots.featherLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FEATHER)))));
        inventory.addItem(ic.create());
        ic = new ItemsCreator(Material.FLINT, I18n.tl("guis.loots.flint"), Arrays.asList(I18n.tl("guis.loots.flintLore", String.valueOf(LootsRules.getInstance().getLoot(Material.FLINT)))));
        inventory.addItem(ic.create());
        ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null);
        inventory.setItem(8, ic.create());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()) {
                case BARRIER:
                    this.player.closeInventory();
                    break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }
}
