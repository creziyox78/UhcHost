package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class Quit implements Listener {
	
	private final UhcHost pl;
	
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
			if(pl.teamUtils.getTeam(player) != null){
				this.pl.teamUtils.unsetTeam(player, this.pl.teamUtils.getTeam(player));
			}
		    this.pl.scoreboardUtil.reset(player);
		    this.pl.gameManager.removePlayer(player, false);
		    if(pl.gameManager.getHost() == player) pl.gameManager.setHost(null);
		}
		else if(gameManager.getGameState() == GameState.STARTED) {
			if(pl.getAllPlayerManager().containsKey(playersUuid)) {
				e.setQuitMessage("[" + ChatColor.RED + "-"+ ChatColor.WHITE+ "] " + player.getDisplayName());
				if(gameManager.isBorder()) {
					pl.getAllPlayerManager().get(playersUuid).setAlive(false);
					Bukkit.broadcastMessage("§c" + 	player.getName() + " has been eliminated !");
				}
			}
			else {
				e.setQuitMessage("");
			}
		}
	}
}
