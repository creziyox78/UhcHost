package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class QuickData {


    private Map<Integer, Consumer<QuickEvent>> slovenlier;
    private Map<Integer, Boolean> slotCancelled;
    private Consumer<InventoryCloseEvent> closeEvent;
    private Map<String, TaskUpdate> updateItem; 
    private boolean closable;
    private Player owner;
    private IQuickInventory iQuickInventory;

    QuickData(boolean closable, IQuickInventory iQuickInventory) {
        this.slovenlier = new HashMap<>();
        this.slotCancelled = new HashMap<>();
        this.updateItem = new HashMap<>();
        this.closable = closable;
        this.iQuickInventory = iQuickInventory;
    }

    Map<Integer, Consumer<QuickEvent>> getSlovenlier() { return slovenlier; }
    Map<Integer, Boolean> getSlotCancelled() { return slotCancelled; }
    Consumer<InventoryCloseEvent> getCloseEvent() { return closeEvent; }
    Map<String, TaskUpdate> getUpdateItem() { return updateItem; }
    void setCloseEvent(Consumer<InventoryCloseEvent> closeEvent){ this.closeEvent = closeEvent; }
    boolean isClosable() { return closable; }
    void setClosable(boolean closable) { this.closable = closable; }
    Player getOwner() { return owner; }
    void setOwner(Player owner) { this.owner = owner; }
    IQuickInventory getiQuickInventory() { return iQuickInventory; }
}
