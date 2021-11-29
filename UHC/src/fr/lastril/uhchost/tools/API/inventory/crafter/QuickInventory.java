package fr.lastril.uhchost.tools.API.inventory.crafter;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.HeadTextures;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class QuickInventory {

    private Inventory inventory;
    private QuickData quickData;
    private final IQuickInventory iQuickInventory;
 
    private QuickInventory(int size, InventoryType inventoryType, String name, boolean closable, IQuickInventory iQuickInventory) {
        if(inventoryType == null){
            inventory = Bukkit.createInventory(null, size, name);
        }else { inventory = Bukkit.createInventory(null, inventoryType, name); }
        quickData = new QuickData(closable, iQuickInventory);
        this.iQuickInventory = iQuickInventory;
    }


    public void addItem(ItemStack itemStack){
        addItem(itemStack, null, true);
    }

    public void addItem(ItemStack itemStack, boolean cancelled){
        addItem(itemStack, null, cancelled);
    }

    public void addItem(ItemStack itemStack, Consumer<QuickEvent> consumer){
        addItem(itemStack, consumer, true);
    }

    public void addItem(ItemStack itemStack, Consumer<QuickEvent> consumer, boolean cancelled){
        int slot = inventory.firstEmpty();
        inventory.addItem(itemStack);
        if(consumer != null) quickData.getSlovenlier().put(slot, consumer);
        quickData.getSlotCancelled().put(slot, cancelled);
    }
    
    /**
     * 
     * 
     * @param inv (if null close the inventory)
     */
    public void addRetourItem(IQuickInventory inv) {
    	this.addRetourItem(inv, quickData.getiQuickInventory().getSize() - 1);
    }

    public void addRetourItem(IQuickInventory inv, int slot) {
        setItem(new QuickItem(Material.SKULL_ITEM, 1, (byte) 3).setName(inv == null ? "§cQuitter" : "§cRetour").setTexture(HeadTextures.RETOUR).toItemStack(), onClick -> {
            if(inv != null)
                inv.open(onClick.getPlayer());
            else
                close(onClick.getPlayer());
        }, slot);
    }


    public void setItem(ItemStack itemStack, int slot){
        setItem(Collections.singletonList(slot), itemStack, null, true);
    }

    public void setItem(ItemStack itemStack, boolean cancelled, int slot){
        setItem(Collections.singletonList(slot), itemStack, null, cancelled);
    }

    public void setItem(ItemStack itemStack, Consumer<QuickEvent> consumer, int slot){
        setItem(Collections.singletonList(slot), itemStack, consumer, true);
    }

    public void setItem(ItemStack itemStack, Consumer<QuickEvent> consumer, boolean cancelled, int slot){
        setItem(Collections.singletonList(slot), itemStack, consumer, cancelled);
    }

    public void setItem(List<Integer> slot, ItemStack itemStack){
        setItem(slot, itemStack, null, true);
    }

    public void setItem(List<Integer> slot, ItemStack itemStack, boolean cancelled){
        setItem(slot, itemStack, null, cancelled);
    }

    public void setItem(List<Integer> slot,ItemStack itemStack, Consumer<QuickEvent> consumer){
        setItem(slot, itemStack, consumer, true);
    }

    public void setItem(List<Integer> slot,ItemStack itemStack, Consumer<QuickEvent> consumer, boolean cancelled){
        for (int i : slot) {
            inventory.setItem(i, itemStack);
            if(consumer != null) quickData.getSlovenlier().put(i, consumer);
            quickData.getSlotCancelled().put(i, cancelled);
        }
    }


    public void setHorizontalLine(ItemStack itemStack, int slotfrom, int slotto) {
        setHorizontalLine(itemStack, slotfrom, slotto, null, true);
    }

    public void setHorizontalLine(ItemStack itemStack, int slotfrom, int slotto, boolean cancelled) {
        setHorizontalLine(itemStack, slotfrom, slotto, null, cancelled);
    }

    public void setHorizontalLine(ItemStack itemStack, int slotfrom, int slotto, Consumer<QuickEvent>  consumer) {
        setHorizontalLine(itemStack, slotfrom, slotto, consumer, true);
    }

    public void setHorizontalLine(ItemStack itemStack, int slotfrom, int slotto, Consumer<QuickEvent>   consumer, boolean cancelled) {
        for (int i = slotfrom; i <= slotto; i++) {
            inventory.setItem(i, itemStack);
            if(consumer != null) quickData.getSlovenlier().put(i, consumer);
            quickData.getSlotCancelled().put(i, cancelled);
        }
    }


    public void setVerticalLine(ItemStack itemStack, int slotfrom, int slotto) {
        setVerticalLine(itemStack, slotfrom, slotto, null, true);
    }

    public void setVerticalLine(ItemStack itemStack, int slotfrom, int slotto, boolean cancelled) {
        setVerticalLine(itemStack, slotfrom, slotto, null, cancelled);
    }

    public void setVerticalLine(ItemStack itemStack, int slotfrom, int slotto, Consumer<QuickEvent>   consumer) {
        setVerticalLine(itemStack, slotfrom, slotto, consumer, true);
    }

    public void setVerticalLine(ItemStack itemStack, int slotfrom, int slotto, Consumer<QuickEvent>   consumer, boolean cancelled) {
        for (int i = slotfrom; i <= slotto; i += 9) {
            inventory.setItem(i, itemStack);
            quickData.getSlotCancelled().put(i, cancelled);
            quickData.getSlovenlier().put(i, consumer);
        }
    }

    public void setQuickEvent(int slot, Consumer<QuickEvent> consumer){
        setQuickEvent(slot, consumer, true);
    }

    public void setQuickEvent(int slot, Consumer<QuickEvent> consumer, boolean cancelled){
        quickData.getSlovenlier().remove(slot);
        quickData.getSlotCancelled().put(slot, cancelled);
        if(consumer != null) quickData.getSlovenlier().put(slot, consumer);
    }


    public void updateItem(String key, Consumer<TaskUpdate> consumer){
        updateItem(key, consumer, 0);
    }

    public void updateItem(String key, Consumer<TaskUpdate> consumer, int period){
        TaskUpdate taskUpdate = new TaskUpdate(consumer);
        quickData.getUpdateItem().put(key, taskUpdate);
        taskUpdate.runTaskTimer(UhcHost.getInstance(), 0, period);
    }

    public TaskUpdate getUpdateItem(String key){
        return quickData.getUpdateItem().get(key);
    }

    public void setClosable(boolean closable){
        quickData.setClosable(closable);
    }

    public void setEventClose(Consumer<InventoryCloseEvent> eventClose){
        quickData.setCloseEvent(eventClose);
    }

    public Player getOwner(){
        return quickData.getOwner();
    }

    void open(Player player){
        player.openInventory(inventory);
    }

    static void open(Player player, IQuickInventory i) {
        if (player.getOpenInventory() != null) {
            if (QuickInventoryManager.getQuickInventories().containsKey(player)) {
                QuickInventory inv = QuickInventoryManager.getQuickInventory(player);
                inv.getQuickData().setClosable(true);
                player.closeInventory();
            }
        }

        if (i.getSize() != 9 && i.getSize() != 9 * 2 && i.getSize() != 9 * 3 && i.getSize() != 9 * 4 && i.getSize() != 9 * 5 && i.getSize() != 9 * 6) {
            throw new ArrayIndexOutOfBoundsException("Error in " + i.getClass().getSimpleName() + " cause : size of inventory is " + i.getSize());
        }

        QuickInventory inventory;
        inventory = new QuickInventory(i.getSize(), i.getInventoryType(), i.getName(), i.isClosable(), i);
        inventory.quickData.setOwner(player);
        i.contents(inventory);
        QuickInventoryManager.getQuickInventories().put(player, inventory);
        inventory.open(player);
    }


    public void close(Player player){
        quickData.setClosable(true);
        player.closeInventory();
    }

    public void update(Player player, IQuickInventory i){
        this.getInventory().clear();
        i.contents(this);
    }
    
    public QuickData getQuickData() { return quickData; }
    public Inventory getInventory() { return inventory; }

    public IQuickInventory getiQuickInventory() {
        return iQuickInventory;
    }
}
