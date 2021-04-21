package fr.lastril.uhchost.player.events.normal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.lastril.uhchost.game.GameState;

public class PlaceBlock implements Listener {
	@EventHandler
	public void onBreak(BlockPlaceEvent e) {
		if (!GameState.isState(GameState.STARTED))
			e.setCancelled(true);
	}
}
