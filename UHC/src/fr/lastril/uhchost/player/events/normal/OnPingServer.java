package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class OnPingServer implements Listener {
	
	private UhcHost pl;
	
	public OnPingServer(UhcHost pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void on(ServerListPingEvent event) {
		event.setMaxPlayers(pl.gameManager.getMaxPlayers());
		if (UhcHost.getInstance().getGamemanager().getGameState() == GameState.REBUILDING) {
			event.setMotd("§7HOST »§9 Rebuilding...");
		
		} else if (UhcHost.getInstance().getGamemanager().getGameState() == GameState.LOBBY) {
			if (Bukkit.hasWhitelist()) {
				event.setMotd("§7                          HOST »§f Whitelist \n" +
						"§7                          Nom »§6 " + pl.gameManager.getGameName());
			} else {
				event.setMotd("§7                         HOST »§7 En attente \n" +
						"§7                          Nom »§6 " + pl.gameManager.getGameName());
			}
		} else {
			event.setMotd("HOST " + UhcHost.getInstance().getGamemanager().getGameState());
		} 
	}
}
