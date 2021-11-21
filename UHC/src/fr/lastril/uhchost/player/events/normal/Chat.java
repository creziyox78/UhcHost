package fr.lastril.uhchost.player.events.normal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setMessage(event.getMessage().replace("&", "§"));
    }

}
