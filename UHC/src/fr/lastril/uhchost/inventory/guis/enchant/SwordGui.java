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

public class SwordGui extends Gui {
    public SwordGui(Player player) {
        super(player, 9*5, ChatColor.LIGHT_PURPLE + "Sword");
        inventory.setItem(4, player.getItemInHand());
        inventory.setItem(19, EnchantBook.Book("Sharpness", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)));
        inventory.setItem(20, EnchantBook.Book("Smite", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)));
        inventory.setItem(21, EnchantBook.Book("Bane of Arthropods", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)));
        inventory.setItem(23, EnchantBook.Book("Knockback", player.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK)));
        inventory.setItem(24, EnchantBook.Book("Fire Aspect", player.getItemInHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT)));
        inventory.setItem(25, EnchantBook.Book("Looting", player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)));
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
            if (is.getType() == Material.ENCHANTED_BOOK)
                if (event.getClick() == ClickType.LEFT) {
                    if (is.getItemMeta().getDisplayName().contains("Sharpness")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ALL);
                    } else if (is.getItemMeta().getDisplayName().contains("Smite")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_UNDEAD);
                    } else if (is.getItemMeta().getDisplayName().contains("Bane of Arthropods")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ARTHROPODS);
                    } else if (is.getItemMeta().getDisplayName().contains("Knockback")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.KNOCKBACK);
                    } else if (is.getItemMeta().getDisplayName().contains("Fire Aspect")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.FIRE_ASPECT);
                    } else if (is.getItemMeta().getDisplayName().contains("Looting")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_MOBS);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                } else if (event.getClick() == ClickType.RIGHT) {
                    if (is.getItemMeta().getDisplayName().contains("Sharpness")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ALL);
                    } else if (is.getItemMeta().getDisplayName().contains("Smite")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_UNDEAD);
                    } else if (is.getItemMeta().getDisplayName().contains("Bane of Arthropods")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ARTHROPODS);
                    } else if (is.getItemMeta().getDisplayName().contains("Knockback")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.KNOCKBACK);
                    } else if (is.getItemMeta().getDisplayName().contains("Fire Aspect")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.FIRE_ASPECT);
                    } else if (is.getItemMeta().getDisplayName().contains("Looting")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_MOBS);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                }
            new SwordGui(player).show();
            if (is.getType() == Material.INK_SACK
                    && is.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
                player.closeInventory();
                new CategoriesGui(player).show();
            }
        }

    }


}
