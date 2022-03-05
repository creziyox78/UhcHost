package fr.lastril.uhchost.modes.sm;

import fr.lastril.uhchost.inventory.scoreboard.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum MarketOwnerLocation {

    LOCATION_RED(new Location(Bukkit.getWorld("lobby"), 1264.5, 65, 405.5, 40F, 2F), TeamUtils.Teams.RED),
    LOCATION_YELLOW(new Location(Bukkit.getWorld("lobby"), 1249.5, 65.0, 400.5, 0.0F, 0.0F), TeamUtils.Teams.YELLOW),
    LOCATION_BLUE(new Location(Bukkit.getWorld("lobby"), 1234.5, 65.0, 405.5, -36.0F, 5.0F), TeamUtils.Teams.BLUE),
    LOCATION_GREEN(new Location(Bukkit.getWorld("lobby"), 1225.5, 65, 417.5, -70.0F, 5.0F), TeamUtils.Teams.GREEN),
    LOCATION_MAGENTA(new Location(Bukkit.getWorld("lobby"), 1225.5, 65, 433.5, -110.0F, 7.0F), TeamUtils.Teams.MAGENTA),
    LOCATION_ORANGE(new Location(Bukkit.getWorld("lobby"), 1234.5, 65, 445.5, -142.0F, 7.0F), TeamUtils.Teams.ORANGE),
    LOCATION_PINK(new Location(Bukkit.getWorld("lobby"), 1249.5, 65, 450.5, 180.0F, 5F), TeamUtils.Teams.PINK),
    LOCATION_LIGHT_BLUE(new Location(Bukkit.getWorld("lobby"), 1264.5, 65, 445.5, 142.0F, 6.5F), TeamUtils.Teams.LIGHT_BLUE),
    LOCATION_LIGHT_GREEN(new Location(Bukkit.getWorld("lobby"), 1273.5, 65.0, 433.5, 110.0F, 8.0F), TeamUtils.Teams.LIGHT_GREEN),
    ;

    private final Location ownerLocation;

    private final TeamUtils.Teams teams;

    MarketOwnerLocation(Location location, TeamUtils.Teams teams) {
        this.ownerLocation = location;
        this.teams = teams;
    }

    public TeamUtils.Teams getTeams() {
        return teams;
    }

    public Location getOwnerLocation() {
        return ownerLocation;
    }
}
