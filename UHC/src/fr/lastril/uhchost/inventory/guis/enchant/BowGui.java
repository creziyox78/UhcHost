package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.EnchantBook;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class BowGui extends Gui {
    public BowGui(Player player) {
        super(player, 9*5, ChatColor.LIGHT_PURPLE + "Bow");
        inventory.setItem(4, player.getItemInHand());
        inventory.setItem(20, EnchantBook.Book("Power", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_DAMAGE)));
        inventory.setItem(21, EnchantBook.Book("Punch", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK)));
        inventory.setItem(23, EnchantBook.Book("Flame", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_FIRE)));
        inventory.setItem(24, EnchantBook.Book("Infinity", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_INFINITE)));
        inventory.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
        inventory.setItem(31, EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)));
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void menuEnchant(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            if (is.getType() == Material.ENCHANTED_BOOK){
                if (event.getClick() == ClickType.LEFT) {
                    if (is.getItemMeta().getDisplayName().contains("Power")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_DAMAGE);
                    } else if (is.getItemMeta().getDisplayName().contains("Punch")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_KNOCKBACK);
                    } else if (is.getItemMeta().getDisplayName().contains("Flame")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_FIRE);
                    } else if (is.getItemMeta().getDisplayName().contains("Infinity")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_INFINITE);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                } else if (event.getClick() == ClickType.RIGHT) {
                    if (is.getItemMeta().getDisplayName().contains("Power")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_DAMAGE);
                    } else if (is.getItemMeta().getDisplayName().contains("Punch")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_KNOCKBACK);
                    } else if (is.getItemMeta().getDisplayName().contains("Flame")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_FIRE);
                    } else if (is.getItemMeta().getDisplayName().contains("Infinity")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_INFINITE);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                }
                new BowGui(player).show();
            } else if (is.getType() == Material.INK_SACK
                    && is.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
                player.closeInventory();
                new CategoriesGui(player).show();
            }
        }

    }

}
