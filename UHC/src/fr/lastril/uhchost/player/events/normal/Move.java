package fr.lastril.uhchost.player.events.normal;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.lastril.uhchost.game.GameState;

public class Move implements Listener {

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (GameState.isState(GameState.PRESTART))
			if (hasMovedIngoreY(event.getTo(), event.getFrom())) {
				player.teleport(event.getFrom());
				return;
			}

	}

	private boolean hasMovedIngoreY(Location l, Location l1) {
		return (l.getX() != l1.getX() || l.getZ() != l1.getZ());
	}

}
