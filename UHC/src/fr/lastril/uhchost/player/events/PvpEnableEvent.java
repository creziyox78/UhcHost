package fr.lastril.uhchost.player.events;

import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class PvpEnableEvent extends Event implements Cancellable{
	
	private final List<Player> players;

	private static final HandlerList handlers = new HandlerList();

	public PvpEnableEvent(List<PlayerManager> players) {
		this.players = new ArrayList<>();
		players.forEach(u -> this.players.add(u.getPlayer()));
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
