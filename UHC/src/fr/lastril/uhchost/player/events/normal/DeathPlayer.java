package fr.lastril.uhchost.player.events.normal;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.events.PlayerKillEvent;

public class DeathPlayer implements Listener {

	private UhcHost hu;

	public DeathPlayer(UhcHost hu) {
	    this.hu = hu;
	  }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeathInGame(PlayerDeathEvent e) {
		Player player = e.getEntity();
		Player killer = player.getKiller();
		GameManager gamemanager = UhcHost.getInstance().getGamemanager();
		if (GameState.isState(GameState.STARTED)) {
			if (gamemanager.getPlayers().contains(player.getUniqueId())) {
				this.hu.gameManager.removePlayer(player, true);
				player.setGameMode(GameMode.SPECTATOR);
				
			}
			if (killer instanceof Player) {
				Bukkit.getPluginManager().callEvent((Event) new PlayerKillEvent(killer));
			}
		}

	}
}
