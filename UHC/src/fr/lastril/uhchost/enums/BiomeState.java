package fr.lastril.uhchost.enums;

import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum BiomeState {

    ROOFED_FOREST(BiomeBase.ROOFED_FOREST, new ItemsCreator(Material.SAPLING, "§aRoof Forest", Arrays.asList("§7Idéal pour des", "§7modes de jeu stratégique !"), 1, (byte) 5).create()),
    FOREST(BiomeBase.FOREST, new ItemsCreator(Material.SAPLING, "§aForêt de chaîne", Arrays.asList("§7Une forêt des plus classiques !"), 1).create()),
    TAIGA(BiomeBase.TAIGA, new QuickItem(Material.SAPLING, 1, (byte)1)
            .setName("§fForêt de sapin")
            .setLore("§7Forêt où pousse", "§7de grands arbres de sapin").toItemStack()),
    PLAINS(BiomeBase.PLAINS, new QuickItem(Material.GRASS)
            .setName("§aPlaine")
            .setLore("§7Une grande pleine d'herbre idéal pour", "§7ne pas avoir d'obstacles sur sa route.").toItemStack()),
    DESERT(BiomeBase.DESERT, new QuickItem(Material.SAND)
            .setName("§eDesert")
            .setLore("§7Un désert chaud et sec").toItemStack()),
    ;

    private final BiomeBase biomeBase;

    private final ItemStack itemBiome;

    BiomeState(BiomeBase biomeBase, ItemStack itemColored) {
        this.biomeBase = biomeBase;
        this.itemBiome = itemColored;
    }

    public BiomeBase getBiomeBase() {
        return biomeBase;
    }

    public ItemStack getItemBiome() {
        return itemBiome;
    }
}
