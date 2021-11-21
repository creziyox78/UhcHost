package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Drop implements Listener {
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (!GameState.isState(GameState.STARTED))
			event.setCancelled(true);
		else
			if(UhcHost.getInstance().gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){
				LoupGarouManager loupGarouManager = (LoupGarouManager) UhcHost.getInstance().gameManager.getModes().getMode().getModeManager();
				event.setCancelled(loupGarouManager.getWaitingRessurect().contains(event.getPlayer().getUniqueId()));
			}
	}
}
