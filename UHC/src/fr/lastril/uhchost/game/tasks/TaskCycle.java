package fr.lastril.uhchost.game.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskCycle extends BukkitRunnable {

	private final UhcHost main;
	private World world;

	public TaskCycle(UhcHost main) {
		this.main = main;
		world = Bukkit.getWorld("game");
	}

	@Override
	public void run() {
		world.setTime((long)((float)world.getTime() + 20.0F * (600.0F / main.gameManager.getCycleTime() - 1.0F)));
		//long time = (long) (Bukkit.getWorld("game").getTime() + (10 / (main.gameManager.getCycleTime() / 60)));
		for(PlayerManager playerManager : main.getPlayerManagerOnlines()){
			if(playerManager.hasRole())
				playerManager.getRole().checkRunnable(playerManager.getPlayer());
		}
		//world.setTime(time);
	}

}
