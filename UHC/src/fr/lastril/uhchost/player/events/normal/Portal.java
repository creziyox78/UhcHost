package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class Portal implements Listener {

	private final UhcHost pl;

	public Portal(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onPortalCreate(PortalCreateEvent event) {
		if (!this.pl.getGamemanager().isNether())
			event.setCancelled(true);
	}

}
