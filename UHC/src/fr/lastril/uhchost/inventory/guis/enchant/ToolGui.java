package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.EnchantBook;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class ToolGui extends IQuickInventory {

    private final Player player;

    public ToolGui(Player player) {
        super(ChatColor.LIGHT_PURPLE + "Tool", 9*5);
        this.player = player;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            inv.setItem(player.getItemInHand(), 4);
            inv.setItem(EnchantBook.Book("Efficiency", player.getItemInHand().getEnchantmentLevel(Enchantment.DIG_SPEED)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.DIG_SPEED);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.DIG_SPEED);
                }
            },19);
            inv.setItem(EnchantBook.Book("Silk Touch", player.getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.SILK_TOUCH);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.SILK_TOUCH);
                }
            },20);
            inv.setItem(EnchantBook.Book("Fortune", player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_BLOCKS);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LOOT_BONUS_BLOCKS);
                }
            },22);
            inv.setItem(EnchantBook.Book("Luck of the Sea", player.getItemInHand().getEnchantmentLevel(Enchantment.LUCK)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LUCK);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LUCK);
                }
            },24);
            inv.setItem(EnchantBook.Book("Lure", player.getItemInHand().getEnchantmentLevel(Enchantment.LURE)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.LURE);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.LURE);
                }
            },25);
            inv.setItem(EnchantBook.Book("Unbreaking", player.getItemInHand().getEnchantmentLevel(Enchantment.DURABILITY)), onClick -> {
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
