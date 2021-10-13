package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.manager.WolfPlayerManager;
import fr.lastril.uhchost.tools.NotStart;
import fr.lastril.uhchost.tools.I18n;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class Join implements Listener {

	private UhcHost pl;

	public Join(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
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

		if(!pl.getAllPlayerManager().containsKey(playersUuid)){
			Bukkit.getConsoleSender().sendMessage("Creating new player data...");
			pl.getAllPlayerManager().put(playersUuid, new PlayerManager(playersUuid));
			Bukkit.getConsoleSender().sendMessage("Created player data !");
		}
		if (gameManager.getGameState() == GameState.LOBBY || gameManager.getGameState() == GameState.STARTING) {
			e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			player.setGameMode(GameMode.ADVENTURE);
			NotStart.PreHosting(player);
			if(player.isOp())
				UpdateMessage(player);
			player.teleport(gameManager.spawn);
			this.pl.scoreboardUtil.reset(player);

		}
		else if(gameManager.getGameState() == GameState.STARTED) {
			PlayerManager playerManager = pl.getPlayerManager(playersUuid);
			player.setScoreboard(this.pl.scoreboardUtil.getBoard());
			if(!playerManager.isAlive()) {
				e.setJoinMessage("");
				player.setGameMode(GameMode.SPECTATOR);
			}
			else {
				e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			}
		}
		Bukkit.getOnlinePlayers().stream().forEach(player1 -> {
			player.hidePlayer(player1);
			player1.hidePlayer(player);
			player.showPlayer(player1);
			player1.showPlayer(player);
		});
	}

	private void UpdateMessage(Player player) {
		player.sendMessage(" ");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.AQUA + "         UHC Host: jour V0.9.1");
		player.sendMessage(ChatColor.AQUA + "                    By Lastril");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GREEN + "+Ajout du scénario DiamondLimit (configurable).");
		player.sendMessage(ChatColor.GREEN + "+Ajout des paramètres du monde.");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GOLD + "*Développement du mode de jeu LG UHC.");
		player.sendMessage(" ");
	}
}
