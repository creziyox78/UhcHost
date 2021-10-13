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

public class ArmorGui extends Gui {
    public ArmorGui(Player player) {
        super(player, 9*5, ChatColor.LIGHT_PURPLE + "Armor");
        inventory.setItem(4, player.getItemInHand());
        inventory.setItem(18,
                EnchantBook.Book("Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)));
        inventory.setItem(19,
                EnchantBook.Book("Fire Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_FIRE)));
        inventory.setItem(20,
                EnchantBook.Book("Feather Falling", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_FALL)));
        inventory.setItem(21,
                EnchantBook.Book("Blast Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)));
        inventory.setItem(22, EnchantBook.Book("Projectile Protection",
                player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)));
        inventory.setItem(23, EnchantBook.Book("Respiration", player.getItemInHand().getEnchantmentLevel(Enchantment.OXYGEN)));
        inventory.setItem(24, EnchantBook.Book("Aqua Affinity", player.getItemInHand().getEnchantmentLevel(Enchantment.WATER_WORKER)));
        inventory.setItem(25, EnchantBook.Book("Thorns", player.getItemInHand().getEnchantmentLevel(Enchantment.THORNS)));
        inventory.setItem(26, EnchantBook.Book("Depth Strider", player.getItemInHand().getEnchantmentLevel(Enchantment.DEPTH_STRIDER)));
        inventory.setItem(36, Items.getItemColored(Material.INK_SACK, ChatColor.RED + "Back", (byte) 1, true));
        inventory.setItem(31, EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)));
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
                    if (is.getItemMeta().getDisplayName().contains("Fire Protection")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FIRE);
                    } else if (is.getItemMeta().getDisplayName().contains("Feather Falling")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FALL);
                    } else if (is.getItemMeta().getDisplayName().contains("Blast Protection")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_EXPLOSIONS);
                    } else if (is.getItemMeta().getDisplayName().contains("Projectile Protection")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_PROJECTILE);
                    } else if (is.getItemMeta().getDisplayName().contains("Protection")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_ENVIRONMENTAL);
                    } else if (is.getItemMeta().getDisplayName().contains("Respiration")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.OXYGEN);
                    } else if (is.getItemMeta().getDisplayName().contains("Aqua Affinity")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.WATER_WORKER);
                    } else if (is.getItemMeta().getDisplayName().contains("Thorns")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.THORNS);
                    } else if (is.getItemMeta().getDisplayName().contains("Depth Strider")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DEPTH_STRIDER);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                } else if (event.getClick() == ClickType.RIGHT) {
                    if (is.getItemMeta().getDisplayName().contains("Fire Protection")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FIRE);
                    } else if (is.getItemMeta().getDisplayName().contains("Feather Falling")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FALL);
                    } else if (is.getItemMeta().getDisplayName().contains("Blast Protection")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_EXPLOSIONS);
                    } else if (is.getItemMeta().getDisplayName().contains("Projectile Protection")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_PROJECTILE);
                    } else if (is.getItemMeta().getDisplayName().contains("Protection")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_ENVIRONMENTAL);
                    } else if (is.getItemMeta().getDisplayName().contains("Respiration")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.OXYGEN);
                    } else if (is.getItemMeta().getDisplayName().contains("Aqua Affinity")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.WATER_WORKER);
                    } else if (is.getItemMeta().getDisplayName().contains("Thorns")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.THORNS);
                    } else if (is.getItemMeta().getDisplayName().contains("Depth Strider")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DEPTH_STRIDER);
                    } else if (is.getItemMeta().getDisplayName().contains("Unbreaking")) {
                        EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                    }
                }
            }
            new ArmorGui(player).show();
            if (is.getType() == Material.INK_SACK
                    && is.getItemMeta().getDisplayName().contains(ChatColor.RED + "Back")) {
                player.closeInventory();
                new CategoriesGui(player).show();
            }
        }

    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }
}
