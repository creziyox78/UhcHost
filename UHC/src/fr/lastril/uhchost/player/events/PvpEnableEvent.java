package fr.lastril.uhchost.player.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PvpEnableEvent extends Event implements Cancellable{
	
	private List<Player> players;

	private static final HandlerList handlers = new HandlerList();

	public PvpEnableEvent(List<UUID> players) {
		this.players = new ArrayList<>();
		players.forEach(u -> this.players.add(Bukkit.getPlayer(u)));
		setCancelled(false);
	}

	public List<Player> getPlayers() {
		return this.players;
	}

	public boolean isCancelled() {
		return false;
	}

	public void setCancelled(boolean b) {
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
