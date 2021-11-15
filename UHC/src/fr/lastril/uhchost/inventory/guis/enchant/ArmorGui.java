package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.EnchantBook;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ArmorGui extends IQuickInventory {

    private final Player player;

    public ArmorGui(Player player) {
        super("§bArmure", 9*6);
        this.player = player;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            inv.setItem(player.getItemInHand(), 4);
            inv.setItem(EnchantBook.Book("Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_ENVIRONMENTAL);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_ENVIRONMENTAL);
                        }
                    },18);
            inv.setItem(EnchantBook.Book("Fire Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_FIRE)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FIRE);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FIRE);
                        }
                    }, 19);
            inv.setItem(EnchantBook.Book("Feather Falling", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_FALL)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FALL);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_FALL);
                        }
                    },
                    20);
            inv.setItem(EnchantBook.Book("Blast Protection", player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_EXPLOSIONS);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_EXPLOSIONS);
                        }
                    },
                    21);
            inv.setItem(EnchantBook.Book("Projectile Protection",player.getItemInHand().getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.PROTECTION_PROJECTILE);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.PROTECTION_PROJECTILE);
                        }
                    },22);
            inv.setItem(EnchantBook.Book("Respiration", player.getItemInHand().getEnchantmentLevel(Enchantment.OXYGEN)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.OXYGEN);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.OXYGEN);
                        }
                    },23);
            inv.setItem(EnchantBook.Book("Aqua Affinity", player.getItemInHand().getEnchantmentLevel(Enchantment.WATER_WORKER)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.WATER_WORKER);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.WATER_WORKER);
                        }
                    },24);
            inv.setItem(EnchantBook.Book("Thorns", player.getItemInHand().getEnchantmentLevel(Enchantment.THORNS)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.THORNS);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.THORNS);
                        }
                    },25);
            inv.setItem(EnchantBook.Book("Depth Strider", player.getItemInHand().getEnchantmentLevel(Enchantment.DEPTH_STRIDER)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DEPTH_STRIDER);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DEPTH_STRIDER);
                        }
                    },26);
            inv.setItem(EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)),
                    onClick -> {
                        if(onClick.getClickType() == ClickType.LEFT){
                            EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                        } else if(onClick.getClickType() == ClickType.RIGHT) {
                            EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DURABILITY);
                        }
                    },31);
        }, 1);
        inv.addRetourItem(new CategoriesGui(player));

    }
}
