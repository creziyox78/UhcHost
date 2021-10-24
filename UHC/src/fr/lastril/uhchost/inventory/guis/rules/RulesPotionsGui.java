package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
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
import org.bukkit.potion.Potion;

import java.util.Arrays;

public class RulesPotionsGui extends Gui {
    public RulesPotionsGui(Player player) {
        super(player, 54, I18n.tl("guis.rulesPotions.name"));
        for (Potion potion : (UhcHost.getInstance()).gameManager.getDeniedPotions()) {
            inventory.addItem(potion.toItemStack(1));
        }
        ItemsCreator ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Arrays.asList(""));
        inventory.setItem(53, ic.create());
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
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }
}
