package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Drop implements Listener {
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (!GameState.isState(GameState.STARTED))
			event.setCancelled(true);
	}
}
