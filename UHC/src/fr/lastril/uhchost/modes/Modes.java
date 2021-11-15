package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.modes.classic.ClassicMode;
import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum Modes {

    CLASSIC("§fClassique", "", new ClassicMode(), new ItemsCreator(Material.IRON_SWORD, "§fClassique", Arrays.asList(""), 1).create()),
    LOUP_GAROU("§eLoup Garou", "", new LoupGarouMode(),
            new QuickItem(Material.SKULL_ITEM).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY4ZDQzMTI5MzliYjMxMTFmYWUyOGQ2NWQ5YTMxZTc3N2Y4ZjJjOWZjNDI3NTAxY2RhOGZmZTNiMzY3NjU4In19fQ==").setName("§eLoup Garou").toItemStack()),
    /*NARUTO_V2("§6Naruto V2", "", new NarutoV2(),
            Items.createHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ=="
                    , "§cBientôt", 1, null)),*/
    SOON_1("§cBientôt", "", null,
            new QuickItem(Material.SKULL_ITEM)
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ==").setName("§cSoon").toItemStack());

    private final String name, headHash;
    private final Mode mode;
    private final ItemStack item;

    Modes(String name, String headHash, Mode mode, ItemStack item) {
        this.name = name;
        this.headHash = headHash;
        this.mode = mode;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public String getHeadHash() {
        return headHash;
    }

    public Mode getMode() {
        return mode;
    }

    public ItemStack getItem() {
        return item;
    }

}
