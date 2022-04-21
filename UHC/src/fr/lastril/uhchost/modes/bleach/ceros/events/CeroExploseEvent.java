package fr.lastril.uhchost.modes.bleach.ceros.events;

import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CeroExploseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private final CeroType ceroType;

    private final Location exploseLocation;

    public CeroExploseEvent(Location exploseLocation, CeroType ceroType) {
        this.exploseLocation = exploseLocation;
        this.ceroType = ceroType;
    }

    public Player nearPlayer() {
        return exploseLocation.getWorld().getPlayers().stream()
                .filter(player -> player.getLocation().distance(exploseLocation) <= 0.2)
                .findFirst().orElse(null);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Location getExploseLocation() {
        return exploseLocation;
    }

    public CeroType getCeroType() {
        return ceroType;
    }
}
