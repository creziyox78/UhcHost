package fr.lastril.uhchost.test.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class RepulsionUtils {

    public static void ripulseEntityFromLocation(Location location, int distance,int powerMultiply, int powerHigh){
        for(Entity entity : location.getWorld().getNearbyEntities(location,distance, distance, distance)){
            ripulseSpecificEntityFromLocation(entity, location,powerMultiply, powerHigh);
        }
    }

    public static void ripulseSpecificEntityFromLocation(Entity entity, Location location, int powerMultiply, int powerHigh){
        Location initialLocation = location.clone();
        initialLocation.setPitch(0.0f);
        Vector origin = initialLocation.toVector();
        Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(origin);
        fromPlayerToTarget.multiply(powerMultiply); //Multiply the power
        fromPlayerToTarget.setY(powerHigh); // force de repulsion
        entity.setVelocity(fromPlayerToTarget);
    }

}
