package fr.lastril.uhchost.game.rules;

import org.bukkit.Material;

public class BlocksRule {

    public Material id;

    public int data;

    public int round;

    public int minY;

    public int maxY;

    public int size;

    public BlocksRule(Material type, int data, int round, int minY, int maxY, int size) {
        this.id = type;
        this.data = data;
        this.round = round;
        this.minY = minY;
        this.maxY = maxY;
        this.size = size;
    }

}
