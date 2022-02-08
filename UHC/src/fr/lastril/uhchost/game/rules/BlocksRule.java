package fr.lastril.uhchost.game.rules;

import org.bukkit.Material;

public class BlocksRule {

    public Material id;

    public int data;

    public int minY;

    public int maxY;

    public double round;

    public double size;

    public BlocksRule(Material type, int data, double round, int minY, int maxY, double size) {
        this.id = type;
        this.data = data;
        this.round = round;
        this.minY = minY;
        this.maxY = maxY;
        this.size = size;
    }

}
