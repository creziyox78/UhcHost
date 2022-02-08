package fr.lastril.uhchost.modes.ds.speciality.train;

import fr.lastril.uhchost.tools.API.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum TrainLocation {

    TEN(10, new Location(Bukkit.getWorld("world_the_end"), -720.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -726.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -842.0D, 133.0D, 23.0D))),
    NINE(9, new Location(Bukkit.getWorld("world_the_end"), -856.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -862.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -978.0D, 133.0D, 23.0D))),
    EIGHT(8, new Location(Bukkit.getWorld("world_the_end"), -992.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -998.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1114.0D, 133.0D, 23.0D))),
    SEVEN(7, new Location(Bukkit.getWorld("world_the_end"), -1128.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1134.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1250.0D, 133.0D, 23.0D))),
    SIX(6, new Location(Bukkit.getWorld("world_the_end"), -1264.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1270.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1386.0D, 133.0D, 23.0D))),
    FIVE(5, new Location(Bukkit.getWorld("world_the_end"), -1400.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1406.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1522.0D, 133.0D, 23.0D))),
    FOUR(4, new Location(Bukkit.getWorld("world_the_end"), -1536.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1542.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1658.0D, 133.0D, 23.0D))),
    THREE(3, new Location(Bukkit.getWorld("world_the_end"), -1672.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1678.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1794.0D, 133.0D, 23.0D))),
    TWO(2, new Location(Bukkit.getWorld("world_the_end"), -1808.0D, 117.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1814.0D, 118.0D, 7.0D), new Location(Bukkit.getWorld("world_the_end"), -1930.0D, 133.0D, 23.0D))),
    ONE(1, new Location(Bukkit.getWorld("world_the_end"), -1939.0D, 120.0D, 14.0D, 90.0F, 0.0F), new Cuboid(new Location(Bukkit.getWorld("world_the_end"), -1938.0D, 118.0D, 21.0D), new Location(Bukkit.getWorld("world_the_end"), -1971.0D, 127.0D, 6.0D)));

    private final int waggonID;

    private final Location location;

    private final Cuboid waggonCuboid;

    TrainLocation(int waggonID, Location location, Cuboid waggonCuboid) {
        this.waggonID = waggonID;
        this.location = location;
        this.waggonCuboid = waggonCuboid;
    }

    public static TrainLocation getById(int id) {
        for (TrainLocation trainLocation : values()) {
            if (trainLocation.getWaggonID() == id)
                return trainLocation;
        }
        return null;
    }

    public int getWaggonID() {
        return this.waggonID;
    }

    public Location getLocation() {
        return this.location;
    }

    public Cuboid getWaggonCuboid() {
        return this.waggonCuboid;
    }

}
