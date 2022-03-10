package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.PlayerManager;
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
		if (GameState.isState(GameState.LOBBY)|| GameState.isState(GameState.STARTING)) {
			e.setQuitMessage("[" + ChatColor.RED + "-"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			if(pl.teamUtils.getTeam(player) != null){
				this.pl.teamUtils.unsetTeam(player, this.pl.teamUtils.getTeam(player));
			}
		    this.pl.scoreboardUtil.reset(player);
		    this.pl.gameManager.removePlayer(player, true);
			if(pl.gameManager.getHost() == player.getUniqueId()) {
				pl.gameManager.setHostname(null);
				pl.gameManager.setHost(null);
			}
		} else if(GameState.isState(GameState.STARTED)) {
			if(pl.getPlayerManagerAlives().contains(pl.getPlayerManager(playersUuid))) {
				e.setQuitMessage("[" + ChatColor.RED + "-"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			}
			else {
				e.setQuitMessage("");
			}
			PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
			if(playerManager.getDeathLocation() == null){
				playerManager.setDeathLocation(player.getLocation());
			}
		}
	}
}
