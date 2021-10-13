package fr.lastril.uhchost.tools.API.clickable_messages;

import java.util.HashMap;
import java.util.Map;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ClickableMessageManager implements Listener{
	
	private static Map<Player, ClickableMessage> messages;

	public ClickableMessageManager(UhcHost main) {
		main.getServer().getPluginManager().registerEvents(this, main);
		ClickableMessageManager.messages = new HashMap<>();
	}

	public static void addMessage(ClickableMessage message) {
		messages.put(message.getPlayer(), message);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(ClickableMessageManager.messages.containsKey(event.getPlayer())) {
			ClickableMessage message = ClickableMessageManager.messages.get(event.getPlayer());
			if((message.getId()+"").equalsIgnoreCase(event.getMessage())) {
				message.getOnClick().accept(event.getPlayer());
				ClickableMessageManager.messages.remove(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
	
}
