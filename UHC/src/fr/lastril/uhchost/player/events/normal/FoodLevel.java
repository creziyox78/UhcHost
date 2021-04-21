package fr.lastril.uhchost.player.events.normal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.lastril.uhchost.game.GameState;

public class FoodLevel implements Listener {
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if (!GameState.isState(GameState.STARTED)) {
			e.setFoodLevel(20);
		}
	}
}
