package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.EnchantBook;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CategoriesGui extends Gui {
    public CategoriesGui(Player player) {
        super(player, 9*5, ChatColor.LIGHT_PURPLE + "Categories");
        inventory.setItem(3, player.getItemInHand());
        if (player.getItemInHand().getItemMeta().spigot().isUnbreakable()) {
            inventory.setItem(5, EnchantBook.Unbreakable(true));
        } else {
            inventory.setItem(5, EnchantBook.Unbreakable(false));
        }
        inventory.setItem(19, Items.getItem(Material.DIAMOND_SWORD, ChatColor.LIGHT_PURPLE + "Sword", true));
        inventory.setItem(21, Items.getItem(Material.DIAMOND_CHESTPLATE, ChatColor.LIGHT_PURPLE + "Armor", true));
        inventory.setItem(23, Items.getItem(Material.BOW, ChatColor.LIGHT_PURPLE + "Bow", true));
        inventory.setItem(25, Items.getItem(Material.DIAMOND_PICKAXE, ChatColor.LIGHT_PURPLE + "Tool", true));
        inventory.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Close", (byte) 1, true));
    }

    @EventHandler
    public void menuEnchant(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player player = (Player) e.getWhoClicked();
        ItemStack current = e.getCurrentItem();
        ItemStack hand = player.getItemInHand();
        ClickType click = e.getClick();
        e.setCancelled(true);
        if (e.getClickedInventory() == null)
            return;
        if (e.getClickedInventory().equals(inventory)) {
            if (current != null){
                if (current.getType() == Material.BEDROCK) {
                    if (current.hasItemMeta() && current.getItemMeta().hasDisplayName() && current.getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Unbreakable:")) {
                        EnchantBook.SetUnbreakable(hand, !hand.getItemMeta().spigot().isUnbreakable());
                    } else {
                        return;
                    }
                    new CategoriesGui(player).show();
                } else if (current.getType() == Material.DIAMOND_SWORD) {
                    if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Sword")) {
                        player.closeInventory();
                        new SwordGui(player).show();
                    }
                } else if (current.getType() == Material.DIAMOND_CHESTPLATE) {
                    if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Armor")) {
                        player.closeInventory();
                        new ArmorGui(player).show();
                    }
                } else if (current.getType() == Material.BOW) {
                    if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Bow")) {
                        player.closeInventory();
                        new BowGui(player).show();
                    }
                } else if (current.getType() == Material.DIAMOND_PICKAXE) {
                    if (current.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.LIGHT_PURPLE + "Tool")) {
                        player.closeInventory();
                        new ToolGui(player).show();
                    }
                } else if (current.getType() == Material.INK_SACK
                        && current.getItemMeta().getDisplayName().contains(ChatColor.RED + "Close")) {
                    player.closeInventory();
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
