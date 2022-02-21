package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.events.GameStartEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (GameState.isState(GameState.PRESTART)){
			if (hasMovedIngoreY(event.getTo(), event.getFrom())) {
				player.teleport(event.getFrom());
			}
		}

	}

	@EventHandler
	public void onStart(GameStartEvent event){
		HandlerList.unregisterAll(this);
	}

	private boolean hasMovedIngoreY(Location l, Location l1) {
		return (l.getX() != l1.getX() || l.getZ() != l1.getZ());
	}

}
