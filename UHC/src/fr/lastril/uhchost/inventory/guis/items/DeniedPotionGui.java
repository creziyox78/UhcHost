package fr.lastril.uhchost.inventory.guis.items;

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

import java.util.Arrays;

public class DeniedPotionGui extends Gui {

    public DeniedPotionGui(Player player) {
        super(player, 54, I18n.tl("guis.deniedPotions.name", ""));
        for (Potion potion : (UhcHost.getInstance()).gameManager.getDeniedPotions()) {
            ItemStack pot = potion.toItemStack(1);
            ItemMeta m = pot.getItemMeta();
            m.setLore(Arrays.asList(I18n.tl("guis.deniedPotions.lore", "")));
            pot.setItemMeta(m);
            inventory.addItem(pot);
        }
        ItemsCreator ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back", ""), Arrays.asList(""));
        inventory.setItem(53, ic.create());
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
            if (is.getType() == Material.BARRIER) {
                event.getWhoClicked().closeInventory();
                new PotionsGui(this.player).show();
            } else if (is.getType() == Material.POTION) {
                (UhcHost.getInstance()).gameManager.removeDeniedPotion(Potion.fromItemStack(is));
                inventory.clear();
                for (Potion potion : (UhcHost.getInstance()).gameManager.getDeniedPotions()) {
                    ItemStack pot = potion.toItemStack(1);
                    ItemMeta m = pot.getItemMeta();
                    m.setLore(Arrays.asList(I18n.tl("guis.deniedPotions.lore", "")));
                    pot.setItemMeta(m);
                    inventory.addItem(pot);
                }
                ItemsCreator ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back", ""), Arrays.asList(""));
                inventory.setItem(53, ic.create());
            }
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

}
