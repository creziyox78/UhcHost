package fr.lastril.uhchost.player.events.normal;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.inventory.NotStart;
import fr.lastril.uhchost.world.tasks.ChunksLoaderTask;
import net.md_5.bungee.api.ChatColor;

public class Join implements Listener {

	private UhcHost pl;

	public Join(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (UhcHost.getInstance().getGamemanager().getGameState() == GameState.REBUILDING
				&& e.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
					"Loading chunk: " + (int) ChunksLoaderTask.getLoaded() + "%");
			e.getPlayer().sendMessage("en cours...:" + (int) ChunksLoaderTask.getLoaded() + "%");
		}
		if (Bukkit.getOnlinePlayers().size() >= this.pl.gameManager.getMaxPlayers()) {
		      e.setResult(PlayerLoginEvent.Result.KICK_FULL);
		      e.setKickMessage(I18n.tl("serverFull", new String[0]));
		    }
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		GameManager gameManager = UhcHost.getInstance().getGamemanager();
		UUID playersUuid = player.getUniqueId();
		if (gameManager.getGameState() == GameState.LOBBY || gameManager.getGameState() == GameState.STARTING) {
			e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			player.setGameMode(GameMode.ADVENTURE);
			NotStart.PreHosting(player);
			UpdateMessage(player);
			player.teleport(gameManager.spawn);
			this.pl.scoreboardUtil.reset(player);
			this.pl.gameManager.addPlayer(player);
		}
		else if(gameManager.getGameState() == GameState.STARTED) {
			player.setScoreboard(this.pl.scoreboardUtil.getBoard());
			if(!gameManager.getPlayers().contains(playersUuid)) {
				e.setJoinMessage("");
				player.setGameMode(GameMode.SPECTATOR);
			}
			else {
				e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			}
		}
	}

	private void UpdateMessage(Player player) {
		player.sendMessage(" ");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.AQUA + "         UHC Host: jour V0.8.2");
		player.sendMessage(ChatColor.AQUA + "                    By Lastril");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GREEN + "+Ajout du nombres de slots maximum configurable.");
		player.sendMessage(ChatColor.GREEN + "+Ajout de la configuration du nether.");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GOLD + "*Les équipes se mettent en fonction du nombre de slot de la partie.");
		player.sendMessage(ChatColor.GOLD + "*Le MOTD du serveur change en fonction de l'état du serveur.");
		player.sendMessage(" ");
	}
}
