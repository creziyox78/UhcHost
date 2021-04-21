package fr.lastril.uhchost.player.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

public class TeamUnregisteredEvent extends Event implements Cancellable {

	private Team team;

	private static final HandlerList handlers = new HandlerList();

	public TeamUnregisteredEvent(Team team) {
		this.team = team;
		setCancelled(false);
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
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
