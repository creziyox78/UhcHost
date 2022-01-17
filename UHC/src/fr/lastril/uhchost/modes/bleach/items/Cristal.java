package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public class Cristal extends QuickItem {
    public Cristal(UhcHost main) {
        super(Material.PACKED_ICE);
        super.setName("§bCristal");
        super.setLore("",
                "§7Créer une zone de 7x7 qui donne",
                "§dRégénération 2§7 à tous les §9Shinigamis§7.",
                "§7présent à l'intérieur. Si une zone existe",
                "§7déjà, elle sera remplacée par la nouvelle zone.",
                "",
                "§fObjet à poser au sol. (Cooldown - 10 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
    }
}
