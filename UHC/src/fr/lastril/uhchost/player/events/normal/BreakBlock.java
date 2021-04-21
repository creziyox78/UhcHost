package fr.lastril.uhchost.player.events.normal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.lastril.uhchost.game.GameState;

public class BreakBlock implements Listener {
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!GameState.isState(GameState.STARTED))
			e.setCancelled(true);
	}
}
