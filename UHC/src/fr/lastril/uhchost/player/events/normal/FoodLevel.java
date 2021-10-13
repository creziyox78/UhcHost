package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevel implements Listener {
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e) {
		if (!GameState.isState(GameState.STARTED)) {
			e.setFoodLevel(20);
		}
	}
}
