package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.java.JavaPlugin;

class QuickListener implements Listener {

    private JavaPlugin javaPlugin;
    private QuickInventoryManager quickInventoryManager;
 
    public QuickListener(JavaPlugin javaPlugin, QuickInventoryManager quickInventoryManager) {
        this.javaPlugin = javaPlugin;
        this.quickInventoryManager = quickInventoryManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        QuickInventory inv = QuickInventoryManager.getQuickInventory(player);
        if (inv != null) {
            if(event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof Player) {
                event.setCancelled(true);
                return;
            }
            QuickData quickEventData = inv.getQuickData();
            QuickEvent quickEvent = new QuickEvent(event);

            System.out.println("[GUI] "+player.getName()+" clicked on slot "+slot+" in "+inv.getiQuickInventory().getClass().getSimpleName());

            if (quickEventData.getSlotCancelled().containsKey(slot))
                if (quickEventData.getSlotCancelled().get(slot)) event.setCancelled(true);
            if (quickEventData.getSlovenlier().containsKey(slot))
            	if(quickEventData.getSlovenlier() != null)
                	if(quickEventData.getSlovenlier().get(slot) != null)
                quickEventData.getSlovenlier().get(slot).accept(quickEvent);

        }
    }


	@EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        QuickInventory inv = quickInventoryManager.getQuickInventory(player);
        if (inv != null) {
            if (inv.getQuickData().getCloseEvent() != null) inv.getQuickData().getCloseEvent().accept(event);
            if (!inv.getQuickData().isClosable()) {
                Bukkit.getScheduler().runTaskLater(javaPlugin, () -> inv.open(player), 2);
            }else {
                inv.getQuickData().getUpdateItem().forEach((s, taskUpdate) -> taskUpdate.cancel());
                quickInventoryManager.getQuickInventories().remove(player);
            }
        }

    }

	@EventHandler
    public void onDisable(PluginDisableEvent event){
        Bukkit.getOnlinePlayers().forEach(o -> {
            if(quickInventoryManager.getQuickInventories().containsKey(o)) o.closeInventory();
        });
    }
}
