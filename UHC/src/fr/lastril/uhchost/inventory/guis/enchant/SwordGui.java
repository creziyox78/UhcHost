package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.EnchantBook;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class SwordGui extends IQuickInventory {

    private final Player player;

    public SwordGui(Player player) {
        super(ChatColor.LIGHT_PURPLE + "Sword", 9*5);
        this.player = player;
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            inv.setItem(player.getItemInHand(), 4);
            inv.setItem(EnchantBook.Book("Sharpness", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ALL);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ALL);
                }
            },19);

            inv.setItem(EnchantBook.Book("Smite", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_UNDEAD);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_UNDEAD);
                }
            }, 20);
            inv.setItem(EnchantBook.Book("Bane of Arthropods", player.getItemInHand().getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ARTHROPODS);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DAMAGE_ARTHROPODS);
                }
            },21);
            inv.setItem(EnchantBook.Book("Knockback", player.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.KNOCKBACK);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.KNOCKBACK);
                }
            },23);
            inv.setItem(EnchantBook.Book("Fire Aspect", player.getItemInHand().getEnchantmentLevel(Enchantment.FIRE_ASPECT)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.FIRE_ASPECT);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.FIRE_ASPECT);
                }
            },24);
            inv.setItem(EnchantBook.Book("Looting", player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_MOBS);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_MOBS);
                }
            },25);
            inv.setItem(EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)), onClick -> {
                if (onClick.getClickType() == ClickType.LEFT) {
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                } else if (onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                }
            },31);
        }, 1);
        inv.addRetourItem(new CategoriesGui(player));
    }
}
