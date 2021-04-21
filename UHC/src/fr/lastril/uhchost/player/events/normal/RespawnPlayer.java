package fr.lastril.uhchost.player.events.normal;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;

public class RespawnPlayer implements Listener {
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		GameManager gamemanager = UhcHost.getInstance().getGamemanager();
		if (gamemanager.getGameState() == GameState.STARTED) {
			if(!gamemanager.getPlayers().contains(player.getUniqueId())) {
				player.setGameMode(GameMode.SPECTATOR);
			}
		}
	}

}
