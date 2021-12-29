package fr.lastril.uhchost.modes.naruto.v2.generation;

import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;

import java.util.List;

public class MeteoresGenerator {

    public static final String METEORE_KEY = "MeteoreFallingBlock";


    public static void spawnMeteore(Location loc, int radius, int distanceSound){
        for(Player players : Bukkit.getOnlinePlayers()) {
            if(WorldUtils.getDistanceBetweenTwoLocations(loc, players.getLocation()) <= distanceSound) {
                players.playSound(players.getLocation(), Sound.WITHER_SPAWN, 1, 1);
            }
        }

        Location center = loc.clone().add(0, 50, 0);

        List<Location> blockLocations = WorldUtils.generateSphere(center, radius, false);

        for (Location blockLocation : blockLocations) {
            FallingBlock fallingBlock = center.getWorld().spawnFallingBlock(blockLocation, Material.STONE, (byte)0);
            fallingBlock.setDropItem(false);
            fallingBlock.setHurtEntities(true);
            fallingBlock.setCustomName(METEORE_KEY);
        }
    }

}
