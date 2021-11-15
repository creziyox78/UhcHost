package fr.lastril.uhchost.tools.API.items.crafter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

class QuickListeners implements Listener {

    private QuickItemManager quickItemManager;

    QuickListeners(QuickItemManager quickItemManager) {
        this.quickItemManager = quickItemManager;
    }

    @EventHandler 
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if(itemStack == null)return;
        Consumer<QuickEvent> consumer = quickItemManager.getEventQuickItem(itemStack);
        if(consumer != null){
            System.out.println("[ITEMS] "+player.getName()+" clicked on "+itemStack);
            consumer.accept(new QuickEvent(event));
        }
    }



}
