package fr.lastril.uhchost.player.events.normal;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;

public class Quit implements Listener {
	
	private UhcHost pl;
	
	public Quit(UhcHost pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		GameManager gameManager = UhcHost.getInstance().getGamemanager();
		UUID playersUuid = player.getUniqueId();
		if (gameManager.getGameState() == GameState.LOBBY || gameManager.getGameState() == GameState.STARTING) {
			e.setQuitMessage("[" + ChatColor.RED + "-"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			this.pl.teamUtils.unsetTeam(player, this.pl.teamUtils.getTeam(player));
		    this.pl.scoreboardUtil.reset(player);
		    this.pl.gameManager.removePlayer(player, false);
		}
		else if(gameManager.getGameState() == GameState.STARTED) {
			if(gameManager.getPlayers().contains(playersUuid)) {
				e.setQuitMessage("[" + ChatColor.RED + "-"+ ChatColor.WHITE+ "] " + player.getDisplayName());
				if(gameManager.isBorder()) {
					gameManager.getPlayers().remove(playersUuid);
					Bukkit.broadcastMessage("§c" + 	player.getName() + " has been eliminated !");
				}
			}
			else {
				e.setQuitMessage("");
			}
		}
	}
}
