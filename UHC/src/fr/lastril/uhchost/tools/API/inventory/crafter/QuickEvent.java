package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class QuickEvent {

    private InventoryClickEvent event;
    public QuickEvent(InventoryClickEvent event) { 
        this.event = event;
    }

    public Player getPlayer(){
        return (Player) event.getWhoClicked();
    }

    public ClickType getClickType(){
        return event.getClick();
    }

    public InventoryAction getInventoryAction(){
        return event.getAction();
    }

    public InventoryClickEvent getEvent(){
        return event;
    }
}
