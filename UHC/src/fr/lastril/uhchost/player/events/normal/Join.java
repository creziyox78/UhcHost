package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.sm.MarketStatus;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.MarketPlayerManager;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.NotStart;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class Join implements Listener {

	private final UhcHost pl;

	public Join(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void PreLogin(AsyncPlayerPreLoginEvent event){
		String playerName = event.getName();
		Player player = Bukkit.getPlayer(playerName);
		if(player != null && player.isOnline()){
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cUn joueur connecté possède déjà ce pseudo.");
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if (Bukkit.getOnlinePlayers().size() >= this.pl.gameManager.getMaxPlayers() && !e.getPlayer().isOp()) {
		      e.setResult(PlayerLoginEvent.Result.KICK_FULL);
		      e.setKickMessage(I18n.tl("serverFull"));
		}
		if(GameState.isState(GameState.TELEPORTING) || GameState.isState(GameState.PRESTART)){
			e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			e.setKickMessage("§cLa partie est train de se lancer. Attendez qu'elle soit démarré pour rejoindre en tant que spectateur.");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		GameManager gameManager = UhcHost.getInstance().getGamemanager();
		UUID playersUuid = player.getUniqueId();
		this.pl.scoreboardUtil.reset(player);
		if(!pl.getAllPlayerManager().containsKey(playersUuid)){
			Bukkit.getConsoleSender().sendMessage("Creating new player data...");
			pl.getAllPlayerManager().put(playersUuid, new PlayerManager(playersUuid));
			Bukkit.getConsoleSender().sendMessage("Created player data !");
		}
		UhcHost.debug("Checking state: " + GameState.getCurrentState().name());
		if (GameState.isState(GameState.STARTING) || GameState.isState(GameState.LOBBY)) {
			UhcHost.debug("Checking host...");
			e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			player.setGameMode(GameMode.ADVENTURE);
			player.setTotalExperience(0);
			player.setExp(0);
			player.setLevel(0);
			player.setFoodLevel(20);
			player.setSaturation(20);
			player.getInventory().clear();
			PlayerManager playerManager = pl.getPlayerManager(playersUuid);
			if(pl.getGamemanager().getModes() == Modes.SM){
				MarketPlayerManager marketPlayerManager = playerManager.getMarketPlayerManager();
				if(!MarketStatus.getInstance().isMarketStatus(MarketStatus.STARTED)){
					player.teleport(gameManager.spawn);
				}
				if(marketPlayerManager.getTeams() != null){
					pl.teamUtils.setTeam(player, marketPlayerManager.getTeams().getTeam());
				}
			} else {
				player.teleport(gameManager.spawn);
			}
		}  else if(GameState.isState(GameState.STARTED)) {
			PlayerManager playerManager = pl.getPlayerManager(playersUuid);
			UhcHost.debug("Game started, checking player... alive: " + playerManager.isAlive() + ", played: " + playerManager.isPlayedGame());
			if(!playerManager.isAlive() || !playerManager.isPlayedGame()) {
				e.setJoinMessage("");
				player.setGameMode(GameMode.SPECTATOR);
			}
			else {
				e.setJoinMessage("[" + ChatColor.GREEN + "+"+ ChatColor.WHITE+ "] " + player.getDisplayName());
			}
		}
		Bukkit.getOnlinePlayers().forEach(player1 -> {
			player.hidePlayer(player1);
			player1.hidePlayer(player);
			player.showPlayer(player1);
			player1.showPlayer(player);
		});
		if(pl.isListHost(playersUuid)){
			if(pl.gameManager.getHost() == null){
				UhcHost.debug("Set player " + player.getName() + " host !");
				pl.gameManager.setHost(player.getUniqueId());
				pl.gameManager.setHostname(player.getName());

			}
		}
		if(!GameState.isState(GameState.STARTED))
			NotStart.PreHosting(player);
	}
}
