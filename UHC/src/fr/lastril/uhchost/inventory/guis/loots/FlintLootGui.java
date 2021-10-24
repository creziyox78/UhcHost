package fr.lastril.uhchost.inventory.guis.loots;


import fr.lastril.uhchost.game.LootsRules;
import fr.lastril.uhchost.inventory.guis.timer.LootsGui;
import fr.lastril.uhchost.scenario.gui.TimerGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class FlintLootGui extends TimerGui {
    public FlintLootGui(Player player) {
        super(player, I18n.tl("guis.flintLoot.name"));
        ItemsCreator ic = new ItemsCreator(Material.FLINT, "§a"+ LootsRules.getInstance().getLoot(Material.FLINT) + "%", Collections.singletonList(I18n.tl("guis.flintLoot.lore")));
                inventory.setItem(4, ic.create());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            String name;
            int value;
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()) {
                case FLINT:
                    this.player.closeInventory();
                    (new LootsGui(this.player)).show();
                    break;
                case BANNER:
                    name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
                    value = LootsRules.getInstance().getLoot(Material.FLINT) + Integer.parseInt(name);
                    if (value < 0 || value > 100)
                        break;
                    LootsRules.getInstance().setLoot(Material.FLINT, value);
                    ic = new ItemsCreator(Material.FLINT, "§a"+ LootsRules.getInstance().getLoot(Material.FLINT) + "%", Collections.singletonList(I18n.tl("guis.flintLoot.lore")));
                            inventory.setItem(4, ic.create());
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
