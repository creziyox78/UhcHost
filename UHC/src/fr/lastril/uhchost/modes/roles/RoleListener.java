package fr.lastril.uhchost.modes.roles;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public interface RoleListener extends Listener {

	default void registerListener(UhcHost main, PluginManager pm) {
		pm.registerEvents(this, main);
	}
	
}