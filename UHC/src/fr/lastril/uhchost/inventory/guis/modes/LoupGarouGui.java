package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.CompositionGui;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class LoupGarouGui extends Gui {

    private final LoupGarouMode lgMode;

    public LoupGarouGui(Player player, LoupGarouMode lgMode) {
        super(player, 9*3, "§eLoupGarou");
        this.lgMode = lgMode;
        inventory.setItem(3, new ItemsCreator(Material.BOOK, "§aComposition", null, 1).create());
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            if(is.getType() == Material.BOOK){
                player.closeInventory();
                new CompositionGui(player).show();
            }
        }

    }


}
