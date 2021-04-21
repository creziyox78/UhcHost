package fr.lastril.uhchost.player.events.normal;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.world.tasks.ChunksLoaderTask;

public class OnPingServer implements Listener {
	
	private UhcHost pl;
	
	public OnPingServer(UhcHost pl) {
		this.pl = pl;
	}
	
	@EventHandler
	public void on(ServerListPingEvent event) {
		event.setMaxPlayers(pl.gameManager.getMaxPlayers());
		if (UhcHost.getInstance().getGamemanager().getGameState() == GameState.REBUILDING) {
			event.setMotd("HOST Rebuilding: " + (int) ChunksLoaderTask.getLoaded() + "% " + "");
		
		} else if (UhcHost.getInstance().getGamemanager().getGameState() == GameState.LOBBY) {
			if (Bukkit.hasWhitelist()) {
				event.setMotd("HOST Whitelist ");
			} else {
				event.setMotd("HOST Waiting... ");
			}
		} else {
			event.setMotd("HOST " + UhcHost.getInstance().getGamemanager().getGameState());
		} 
	}
}
