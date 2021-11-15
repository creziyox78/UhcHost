package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public abstract class IQuickInventory {

	private String name;
    private int size;
    private InventoryType inventoryType;
    private boolean closable;
 
    public IQuickInventory(String name, int size) {
        this.name = name;
        this.size = size;
        this.closable = true;
    }

    public IQuickInventory(String name, InventoryType inventoryType) {
        this.name = name;
        this.inventoryType = inventoryType;
    }

    public String getName() { return name; }
    public int getSize() { return size; }

    public InventoryType getInventoryType() { return inventoryType; }
    
    public void setInventoryType(InventoryType inventoryType) { this.inventoryType = inventoryType; }

    public boolean isClosable() { return closable; }
    public void setClosable(boolean closable) { this.closable = closable; }

    public void open(Player player){
        System.out.println("[GUIS] Opened "+this.getClass().getSimpleName()+" gui for "+player.getName());
        QuickInventory.open(player, this);
    }

    public List<QuickInventory> getPlayersOpenInventory(){
        return QuickInventoryManager.getPlayerOpenInventory(this);
    }

    public abstract void contents(QuickInventory inv);
}
