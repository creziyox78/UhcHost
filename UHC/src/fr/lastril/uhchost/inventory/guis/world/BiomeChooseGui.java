package fr.lastril.uhchost.inventory.guis.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.BiomeState;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class BiomeChooseGui extends Gui {

    private final UhcHost pl = UhcHost.getInstance();

    public BiomeChooseGui(Player player) {
        super(player, 3*9, "§bChoix du biome");
        int index = 10;
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, Items.getItemColored(Material.STAINED_GLASS_PANE, " ", (byte) 7, true));
        }
        for(BiomeState biomeState : BiomeState.values()){
            ItemStack is = biomeState.getItemBiome();
            inventory.setItem(index, is);
            index++;
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
            for(BiomeState biomeState : BiomeState.values()){
                if(is.isSimilar(biomeState.getItemBiome())){
                    pl.gameManager.setBiomeState(biomeState);
                    player.closeInventory();
                    new BiomeChooseGui(player).show();
                    return;
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
