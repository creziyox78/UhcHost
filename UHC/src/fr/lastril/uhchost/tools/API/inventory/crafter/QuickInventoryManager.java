package fr.lastril.uhchost.tools.API.inventory.crafter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickInventoryManager {

    private static Map<Player, QuickInventory> quickInventories;
    public QuickInventoryManager(JavaPlugin javaPlugin) { 
        quickInventories = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(new QuickListener(javaPlugin,this), javaPlugin);
    }
    static QuickInventory getQuickInventory(Player player){
        return quickInventories.get(player);
    }

    static Map<Player, QuickInventory> getQuickInventories() {
        return quickInventories;
    }

    static List<QuickInventory> getPlayerOpenInventory(IQuickInventory iQuickInventory){
        List<QuickInventory> inventories = new ArrayList<>();
        quickInventories.forEach((player, quickInventory) -> { if(quickInventory.getQuickData().getiQuickInventory().getClass() == iQuickInventory.getClass()) inventories.add(quickInventory);});
        return inventories;
    }

}
