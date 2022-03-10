package fr.lastril.uhchost.game.tasks;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskCycle extends BukkitRunnable {

	private final UhcHost main;
	private final World world;

	public TaskCycle(UhcHost main) {
		this.main = main;
		world = Bukkit.getWorld("game");
	}

	@Override
	public void run() {
		long time = (long) (Bukkit.getWorld("game").getTime() + (10 / (main.gameManager.getCycleTime() / 60)));
		world.setTime(time);
	}

}
