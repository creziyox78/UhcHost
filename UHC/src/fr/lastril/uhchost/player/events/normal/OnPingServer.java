package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class OnPingServer implements Listener {
	
	private final UhcHost pl;
	
	public OnPingServer(UhcHost pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void on(ServerListPingEvent event) {
		event.setMaxPlayers(pl.gameManager.getMaxPlayers());
		if (GameState.isState(GameState.REBUILDING)) {
			event.setMotd("§7HOST »§9 Rebuilding...");
		} else if (GameState.isState(GameState.LOBBY)) {
			if (Bukkit.hasWhitelist()) {
				event.setMotd("§7                          HOST »§f Sous Whitelist \n" +
						"§7                          Nom »§6 " + pl.gameManager.getGameName());
			} else {
				event.setMotd("§7                         HOST »§7 En attente \n" +
						"§7                          Nom »§6 " + pl.gameManager.getGameName());
			}
		} else if(GameState.isState(GameState.STARTED)){
			event.setMotd("§7                         HOST »§aPartie en cours \n" +
					"§7                          Nom »§6 " + pl.gameManager.getGameName());
		} else if(GameState.isState(GameState.ENDED)){
			event.setMotd("§7                         HOST »§c Partie terminée \n" +
					"§7                          Nom »§6 " + pl.gameManager.getGameName());
		} else if(GameState.isState(GameState.STARTING) || GameState.isState(GameState.TELEPORTING)){
			event.setMotd("§7                         HOST »§6 Démarrage \n" +
					"§7                          Nom »§6 " + pl.gameManager.getGameName());
		} else {
			event.setMotd("§7                         HOST »§7 Unknown \n" +
					"§7                          Nom »§6 " + pl.gameManager.getGameName());
		}
	}
}
