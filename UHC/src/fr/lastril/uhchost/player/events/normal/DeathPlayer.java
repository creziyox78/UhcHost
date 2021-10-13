package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
			if (killer != null) {
				Bukkit.getPluginManager().callEvent(new PlayerKillEvent(killer));
			}
		}
		if (killer != null) {
			gamemanager.getModes().getMode().onDeath(player, killer);
		} else {
			gamemanager.getModes().getMode().onDeath(player, null);
		}

		Bukkit.getScheduler().runTaskLater(hu, () -> {
			player.spigot().respawn();
			player.teleport(new Location(hu.gameManager.spawn.getWorld(), hu.gameManager.spawn.getX(), 192, hu.gameManager.spawn.getZ()));
		}, 20* 2);

	}
}
