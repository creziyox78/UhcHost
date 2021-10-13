package fr.lastril.uhchost.enums;

import fr.lastril.uhchost.tools.creators.ItemsCreator;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum BiomeState {

    ROOFED_FOREST(BiomeBase.ROOFED_FOREST, new ItemsCreator(Material.SAPLING, "§aRoof Forest", Arrays.asList("§7Idéal pour des", "§7modes de jeu stratégique !"), 1, (byte) 5).create()),
    FOREST(BiomeBase.FOREST, new ItemsCreator(Material.SAPLING, "§aForêt de chaîne", Arrays.asList("§7Une forêt des plus classiques !"), 1).create());

    private BiomeBase biomeBase;

    private ItemStack itemBiome;

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
