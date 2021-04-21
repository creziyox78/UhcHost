package fr.lastril.uhchost.player.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event implements Cancellable {
	private Player player;

	private static final HandlerList handlers = new HandlerList();

	public PlayerKillEvent(Player player) {
		this.player = player;
		setCancelled(false);
	}

	public Player getPlayer() {
		return this.player;
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
