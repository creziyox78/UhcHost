package fr.lastril.uhchost.player.events.command;

import fr.lastril.uhchost.enums.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandSendTell implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.getMessage().startsWith("/me")){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Messages.error("Cette commande est désactivé."));
        }
    }

}
