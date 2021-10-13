package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ModesGui extends Gui {

    private final UhcHost pl = UhcHost.getInstance();

    public ModesGui(Player player) {
        super(player, 9*3, ChatColor.AQUA + "Modes");
        int index = 7;
        for (Modes mode : Modes.values()) {
            index += Modes.values().length;
            ItemStack is = mode.getItem();
            if(mode.getMode() instanceof ModeConfig){
                ItemMeta itemMeta = is.getItemMeta();
                itemMeta.setLore(Arrays.asList("", "§6Clique droit§7 pour configurer", "§7le mode de jeu."));
                is.setItemMeta(itemMeta);
            }
            inventory.setItem(index, is);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            for(Modes modes : Modes.values()){
                if(event.getClick() == ClickType.LEFT){
                    if(is.isSimilar(modes.getItem()) && modes != Modes.SOON_1){
                        pl.gameManager.setModes(modes);
                        player.closeInventory();
                        new ModesGui(player).show();
                    }
                } else if(event.getClick() == ClickType.RIGHT){
                    if(is.isSimilar(modes.getItem()) && modes.getMode() instanceof ModeConfig){
                        player.closeInventory();
                        ModeConfig config = (ModeConfig) modes.getMode();
                        config.getGui(player).show();
                    }
                }
            }
        }

    }


    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }


}
