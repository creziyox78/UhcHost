package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathPlayer implements Listener {

	private final UhcHost main;

	public DeathPlayer(UhcHost main) {
	    this.main = main;
	  }

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeathInGame(PlayerDeathEvent e) {
		Player player = e.getEntity();
		Player killer = player.getKiller();
		if (GameState.isState(GameState.STARTED)) {
			if (killer != null) {
				Bukkit.getPluginManager().callEvent(new PlayerKillEvent(player, killer));

			}
			main.getGamemanager().getModes().getMode().onDeath(player, killer);
		} else {
			Bukkit.getScheduler().runTaskLater(main, () -> {
				player.spigot().respawn();
				player.teleport(new Location(main.gameManager.spawn.getWorld(), main.gameManager.spawn.getX(), main.gameManager.spawn.getY() + 5, main.gameManager.spawn.getZ()));
			}, 20* 2);
		}
	}
}
