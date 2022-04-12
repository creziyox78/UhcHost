package fr.lastril.uhchost.modes.bleach.ceros;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCero {

    private final UhcHost main = UhcHost.getInstance();
    private final String name;
    private final List<String> lore;
    private final CeroType type;
    private final int speed;
    private final byte color;

    protected AbstractCero(String name, CeroType type, int speed, byte color) {
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.color = color;
        this.lore = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public CeroType getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }

    public void addLore(String lore) {
        this.lore.add(lore);
    }

    public abstract void action();

    public byte getColor() {
        return color;
    }

    public void giveCero(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.INK_SACK, 1, color)
                .setName(name)
                .setLore(lore)
                .onClick(onClick -> action()).toItemStack());
    }
}
