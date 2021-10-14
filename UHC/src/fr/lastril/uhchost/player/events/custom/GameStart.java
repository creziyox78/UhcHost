package fr.lastril.uhchost.player.events.custom;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.tasks.CycleTask;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.manager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.UUID;

public class GameStart implements Listener {
	
	private UhcHost pl;
	
	public GameStart(UhcHost pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onStart(GameStartEvent e) {
		new CycleTask(pl).runTaskTimer(pl, 0, 1);
		e.getPlayers().forEach(player -> {
			pl.getPlayerManager(player.getUniqueId()).setPlayedGame(true);
		});
		Bukkit.getWorld("game").setGameRuleValue("keepInventory", "true");
		if(pl.gameManager.isViewHealth()) {
			Objective health = pl.scoreboardUtil.getBoard().registerNewObjective("vie", "health");
			health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			e.getPlayers().forEach(u -> u.setScoreboard(pl.scoreboardUtil.getBoard()));
			e.getPlayers().forEach(u -> u.setHealth(19.8));
			new BukkitRunnable() {

				@Override
				public void run() {
					
					e.getPlayers().forEach(u -> u.setHealth(20));
				}
				
			}.runTaskLater(pl, 10);
		}
	}

}
