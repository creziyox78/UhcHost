package fr.lastril.uhchost.world.tasks;

import com.google.common.base.Strings;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Date;

public class ChunkLoader extends BukkitRunnable {

	private final UhcHost main;

	private final Date start;

	private double percent;

	private double currentChunkLoad;

	private final double totalChunkToLoad;

	private int cx;

	private int cz;

	private final int radius;

	private boolean finished;

	private final World world;

	public ChunkLoader(UhcHost main, int radius) {
		this.main = main;

		radius += 150;
		this.percent = 0.0D;
		this.totalChunkToLoad = Math.pow(radius, 2.0D) / 64.0D;
		this.currentChunkLoad = 0.0D;
		this.cx = -radius;
		this.cz = -radius;

		this.world = Bukkit.getWorld("game");
		this.main.worldUtils.setWorld(world);
		this.main.worldUtils.setCenter(new Location(world, 0.0D, 0, 0.0D));
		this.main.worldBorderUtils.change(1000);
		this.world.setGameRuleValue("doFireTick", "false");
		this.world.setGameRuleValue("naturalRegeneration", "false");

		this.radius = radius;
		this.finished = false;
		main.getGamemanager().setPregen(true);
		
		runTaskTimer(main, 0L, 5L);
		this.start = new Date();
	}

	public void run() {
		for (int i = 0; i < 30 && !this.finished; i++) {
			Location loc = new Location(this.world, this.cx, 0.0D, this.cz);
			loc.getChunk().load(true);
			this.cx = this.cx + 16;
			this.currentChunkLoad = this.currentChunkLoad + 1.0D;
			if (this.cx > this.radius) {
				this.cx = -this.radius;
				this.cz = this.cz + 16;
				if (this.cz > this.radius) {
					this.currentChunkLoad = this.totalChunkToLoad;
					this.finished = true;
				}
			}
		}
		this.percent = this.currentChunkLoad / this.totalChunkToLoad * 100.0D;
		Bukkit.getOnlinePlayers().forEach(player -> {
			if(this.percent < 100)
				ActionBar.sendMessage(player,
					"[" + getProgressBar((int) this.percent, 100, 100, '|', ChatColor.GREEN, ChatColor.GRAY) + "§f] §9"
							+ new DecimalFormat("###.##").format(this.percent) + "%");
		});
		if (this.finished) {
			/*long secondsBetween = DateManager.getSecondsBetweenTwoDate(this.start, new Date());
			 */
			Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage()+"§aLa map a été entièrement pré-générée !");
			cancel();
		}
	}

	public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
			ChatColor notCompletedColor) {
		float percent = (float) current / max;
		int progressBars = (int) (totalBars * percent);
		if(current <= max){
			return Strings.repeat("" + completedColor + symbol, progressBars)
					+ Strings.repeat("" + notCompletedColor + symbol,
					totalBars - progressBars);
		}

		return "";
	}
}