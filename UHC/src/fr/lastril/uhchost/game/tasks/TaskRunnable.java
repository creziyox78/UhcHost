package fr.lastril.uhchost.game.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskRunnable extends BukkitRunnable {

	private final UhcHost main;

	public TaskRunnable(UhcHost main) {
		this.main = main;
	}

	@Override
	public void run() {
		for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
			if(playerManager.hasRole())
				playerManager.getRole().checkRunnable(playerManager.getPlayer());
		}
	}

}
