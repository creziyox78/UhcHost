package fr.lastril.uhchost.game.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CycleTask extends BukkitRunnable {

	private final UhcHost main;

	public CycleTask(UhcHost main) {
		this.main = main;
	}

	@Override
	public void run() {
		long time = (long) (Bukkit.getWorld("game").getTime() + (10 / (main.gameManager.getCycleTime() / 60)));
		for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
			if(playerManager.hasRole())
				playerManager.getRole().checkRunnable(playerManager.getPlayer());
		}
		Bukkit.getWorld("game").setTime(time);
	}

}
