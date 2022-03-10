package fr.lastril.uhchost.modes.sm;

import fr.lastril.uhchost.scoreboard.TeamUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public enum MarketOwnerLocation {

    LOCATION_RED(new Location(Bukkit.getWorld("sm_world"), -23.5, 72, 8.5, -180F, 5F), TeamUtils.Teams.RED),
    LOCATION_BLUE(new Location(Bukkit.getWorld("sm_world"), -14.5, 72, 20.5, -142F, 3.0F), TeamUtils.Teams.BLUE),
    LOCATION_GREEN(new Location(Bukkit.getWorld("sm_world"), 0.5, 70, 25.5, -178.0F, 2.5F), TeamUtils.Teams.GREEN),
    LOCATION_YELLOW(new Location(Bukkit.getWorld("sm_world"), -15.5, 70, 21.5, 145F, 1.0F), TeamUtils.Teams.YELLOW),
    LOCATION_MAGENTA(new Location(Bukkit.getWorld("sm_world"), 24.5, 72, 8.5, 107.8F, 4.0F), TeamUtils.Teams.MAGENTA),
    LOCATION_ORANGE(new Location(Bukkit.getWorld("sm_world"), 24.5, 72, -7.5, 71.3F, 3.6F), TeamUtils.Teams.ORANGE),
    LOCATION_PINK(new Location(Bukkit.getWorld("sm_world"), 15.5, 70, -19.5, 38.6F, -1F), TeamUtils.Teams.PINK),
    LOCATION_LIGHT_BLUE(new Location(Bukkit.getWorld("sm_world"), 0.5, 70, -24.5, 0F, 3.5F), TeamUtils.Teams.LIGHT_BLUE),
    LOCATION_LIGHT_GREEN(new Location(Bukkit.getWorld("sm_world"), -14.5, 70, -19.5, -35.8F, 2.2F), TeamUtils.Teams.LIGHT_GREEN),
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
