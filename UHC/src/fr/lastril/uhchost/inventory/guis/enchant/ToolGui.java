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

public class ToolGui extends Gui {
    public ToolGui(Player player) {
        super(player, 9*5, ChatColor.LIGHT_PURPLE + "Tool");
        inventory.setItem(4, player.getItemInHand());
        inventory.setItem(19, EnchantBook.Book("Efficiency", player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED)));
        inventory.setItem(20, EnchantBook.Book("Silk Touch", player.getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH)));
        inventory.setItem(22, EnchantBook.Book("Fortune", player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)));
        inventory.setItem(24, EnchantBook.Book("Luck of the Sea", player.getItemInHand().getEnchantmentLevel(Enchantment.LUCK)));
        inventory.setItem(25, EnchantBook.Book("Lure", player.getItemInHand().getEnchantmentLevel(Enchantment.LURE)));
        inventory.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
        inventory.setItem(31, EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)));
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
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            if (is.getType() == Material.ENCHANTED_BOOK){
                if (event.getClick() == ClickType.LEFT) {
                    if (is.getItemMeta().getDisplayName().contains("Efficiency")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DIG_SPEED);
                    } else if (is.getItemMeta().getDisplayName().contains("Silk Touch")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.SILK_TOUCH);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    } else if (is.getItemMeta().getDisplayName().contains("Fortune")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_BLOCKS);
                    } else if (is.getItemMeta().getDisplayName().contains("Luck of the Sea")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LUCK);
                    } else if (is.getItemMeta().getDisplayName().contains("Lure")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LURE);
                    }
                } else if (event.getClick() == ClickType.RIGHT) {
                    if (is.getItemMeta().getDisplayName().contains("Efficiency")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DIG_SPEED);
                    } else if (is.getItemMeta().getDisplayName().contains("Silk Touch")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.SILK_TOUCH);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    } else if (is.getItemMeta().getDisplayName().contains("Fortune")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_BLOCKS);
                    } else if (is.getItemMeta().getDisplayName().contains("Luck of the Sea")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LUCK);
                    } else if (is.getItemMeta().getDisplayName().contains("Lure")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LURE);
                    }
                }
                new ToolGui(player).show();

            } else if (is.getType() == Material.INK_SACK
                    && is.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
                player.closeInventory();
                new CategoriesGui(player).show();
            }
        }

    }

}
