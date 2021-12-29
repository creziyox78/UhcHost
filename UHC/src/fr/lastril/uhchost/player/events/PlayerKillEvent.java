package fr.lastril.uhchost.player.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event implements Cancellable {
	private final Player player;
	private final Player killer;

	private static final HandlerList handlers = new HandlerList();

	public PlayerKillEvent(Player player, Player killer) {
		this.player = player;
		this.killer = killer;
		setCancelled(false);
	}

	public Player getDeadPlayer() {
		return this.player;
	}

	public boolean isCancelled() {
		return false;
	}

	public void setCancelled(boolean b) {
	}

	public Player getKiller() {
		return killer;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
