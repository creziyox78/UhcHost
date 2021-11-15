package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.EnchantBook;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BowGui extends IQuickInventory {

    private final Player player;

    public BowGui(Player player) {
        super(ChatColor.LIGHT_PURPLE + "Bow", 9*5);
        this.player = player;

    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            inv.setItem(player.getItemInHand(), 4);
            inv.setItem(EnchantBook.Book("Power", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_DAMAGE)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_DAMAGE);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_DAMAGE);
                }
            },20);
            inv.setItem(EnchantBook.Book("Punch", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_KNOCKBACK);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_KNOCKBACK);
                }
            },21);
            inv.setItem(EnchantBook.Book("Flame", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_FIRE)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_FIRE);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_FIRE);
                }
            },23);
            inv.setItem(EnchantBook.Book("Infinity", player.getItemInHand().getEnchantmentLevel(Enchantment.ARROW_INFINITE)), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    EnchantBook.AddEnchantment(player.getItemInHand(), Enchantment.ARROW_INFINITE);
                } else if(onClick.getClickType() == ClickType.RIGHT) {
                    EnchantBook.RemoveEnchantment(player.getItemInHand(), Enchantment.ARROW_INFINITE);
                }
            },24);
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
