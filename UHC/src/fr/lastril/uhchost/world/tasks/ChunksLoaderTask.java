package fr.lastril.uhchost.world.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.world.WorldUtils;

public class ChunksLoaderTask extends BukkitRunnable {
	private UhcHost pl;

	private static double loaded;

	private int oldLoaded;

	private double current;

	private double chunksToLoad;

	private int radius;

	private int x = 0;

	private int z = 0;

	public static double getLoaded() {
		return loaded;
	}

	private boolean ramCheck = false;

	public ChunksLoaderTask(UhcHost pl, int radius) {
		this.pl = pl;
		this.radius = radius;
		this.x = -radius;
		this.z = -radius;
		this.chunksToLoad = Math.pow((radius * 2 / 16), 2.0D);
		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(
				"Loads of " + (int) this.chunksToLoad + " chunks within a radius of " + radius + " blocks");
		Bukkit.getConsoleSender().sendMessage("");
	}

	public void run() {
		Location loc;
		if (!UhcHost.getInstance().getConfig().getBoolean("chunk_load_status"))
			cancel();
		if (this.x >= this.radius) {
			this.x = -this.radius;
			loc = new Location(this.pl.worldUtils.getWorld(), this.x, 64.0D, (this.z += 16));
		} else {
			loc = new Location(this.pl.worldUtils.getWorld(), this.x, 64.0D, this.z);
		}
		WorldUtils.replaceBiomes();
		loc.getChunk().load(true);
		this.current++;
		this.x += 16;
		loaded = this.current / this.chunksToLoad * 100.0D;
		if (this.oldLoaded < (int) loaded) {
			Bukkit.getConsoleSender()
					.sendMessage(String.valueOf(getColor()) + "Chunks loaded at : " + (int) loaded + "%");
			keepAlive();
			this.oldLoaded = (int) loaded;
			UhcHost.getInstance().getGamemanager().setGameState(GameState.REBUILDING);
		}
		if (loaded >= 100.0D) {
			Bukkit.getConsoleSender().sendMessage("Loading of chunks, complete. Total: " + (int) this.chunksToLoad
					+ " Loaded Chunks: " + (this.pl.worldUtils.getWorld().getLoadedChunks()).length);
			UhcHost.getInstance().getGamemanager().setGameState(GameState.LOBBY);
			UhcHost.getInstance().getConfig().set("chunk_load_status", Boolean.valueOf(false));
			UhcHost.getInstance().saveConfig();
			Bukkit.setWhitelist(false);
			System.gc();
			this.pl.taskManager.lobbyTask();
			cancel();
		}
	}

	private void keepAlive() {
		if (getRamUsed() > 50.0D && !this.ramCheck && this.oldLoaded == 50 && !this.ramCheck) {
			this.ramCheck = true;
			Bukkit.getConsoleSender().sendMessage("There is not enough ram. Wait 10 seconds.");
			System.gc();
			try {
				Thread.sleep(10000L);
			} catch (Exception exception) {
			}
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this.pl, new Runnable() {
				public void run() {
					ChunksLoaderTask.this.ramCheck = false;
				}
			}, 60L);
		}
	}

	private double getRamUsed() {
		long RAM_TOTAL = Runtime.getRuntime().totalMemory();
		long RAM_FREE = Runtime.getRuntime().freeMemory();
		long RAM_USED = RAM_TOTAL - RAM_FREE;
		double RAM_USED_PERCENTAGE = RAM_USED * 1.0D / RAM_TOTAL * 100.0D;
		return RAM_USED_PERCENTAGE;
	}

	private String getColor() {
		if (this.oldLoaded < 25)
			return "" + ChatColor.RED;
		if (this.oldLoaded < 50)
			return "" + ChatColor.GOLD;
		if (this.oldLoaded < 75)
			return "" + ChatColor.YELLOW;
		if (this.oldLoaded <= 100)
			return "" + ChatColor.GREEN;
		return null;
	}
}
