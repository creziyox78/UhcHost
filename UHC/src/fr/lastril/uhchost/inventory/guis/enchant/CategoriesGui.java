package fr.lastril.uhchost.inventory.guis.enchant;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.EnchantBook;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CategoriesGui extends IQuickInventory {

    private final Player player;

    public CategoriesGui(Player player) {
        super(ChatColor.LIGHT_PURPLE + "Categories", 9*5);
        this.player = player;
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            inv.setItem(player.getItemInHand(), 3);
            ItemStack hand = player.getItemInHand();
            if (player.getItemInHand().getItemMeta().spigot().isUnbreakable()) {
                inv.setItem(EnchantBook.Unbreakable(true), onClick -> {
                    EnchantBook.SetUnbreakable(hand, !hand.getItemMeta().spigot().isUnbreakable());
                },5);
            } else {
                inv.setItem(EnchantBook.Unbreakable(false), onClick -> {
                    EnchantBook.SetUnbreakable(hand, !hand.getItemMeta().spigot().isUnbreakable());
                },5);
            }

            inv.setItem(new QuickItem(Material.DIAMOND_SWORD).setName(ChatColor.LIGHT_PURPLE + "Sword").toItemStack(), onClick -> {
                player.closeInventory();
                new SwordGui(player).open(player);
            },19);
            inv.setItem(new QuickItem(Material.DIAMOND_CHESTPLATE).setName(ChatColor.LIGHT_PURPLE + "Armor").toItemStack(), onClick -> {
                player.closeInventory();
                new ArmorGui(player).open(player);
            },21);
            inv.setItem(new QuickItem(Material.BOW).setName(ChatColor.LIGHT_PURPLE + "Bow").toItemStack(), onClick -> {
                player.closeInventory();
                new BowGui(player).open(player);
            },23);
            inv.setItem(new QuickItem(Material.DIAMOND_PICKAXE).setName(ChatColor.LIGHT_PURPLE + "Tool").toItemStack(), onClick -> {
                player.closeInventory();
                new ToolGui(player).open(player);
            },25);
        }, 1);
        inv.addRetourItem(new CategoriesGui(player));

    }
}
