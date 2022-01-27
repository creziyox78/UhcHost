package fr.lastril.uhchost.tools.API.items.crafter;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class QuickEvent {

    private PlayerInteractEvent event;
    public QuickEvent(PlayerInteractEvent event) {
        this.event = event;
    }

    public Player getPlayer(){
        return event.getPlayer(); 
    }

    public Action getAction(){
        return event.getAction();
    }

    public void cancelOriginalUse(boolean cancelEvent){
        event.setCancelled(cancelEvent);
    }
}
