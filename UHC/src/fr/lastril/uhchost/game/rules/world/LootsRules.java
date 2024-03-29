package fr.lastril.uhchost.game.rules.world;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootsRules implements Listener{

    private static final Map<Material, Integer> lootsRules = new HashMap<>();

    private static LootsRules instance;

    static {
        lootsRules.put(Material.APPLE, 20);
        lootsRules.put(Material.FLINT, 50);
        lootsRules.put(Material.FEATHER, 20);
        lootsRules.put(Material.STRING, 20);
        lootsRules.put(Material.ENDER_PEARL, 0);
    }

    public LootsRules() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
    }

    public int getLoot(Material material) {
        if (lootsRules.containsKey(material))
            return lootsRules.get(material);
        return 0;
    }

    public void setLoot(Material material, int value) {
        if(value > 99 || value < 1)
            return;
        if (lootsRules.containsKey(material))
            lootsRules.replace(material, value);
    }

    @EventHandler
    public void onLeaveDecay(LeavesDecayEvent event) {
        if ((new Random()).nextInt(100) + 1 <= getLoot(Material.APPLE))
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityType entity = event.getEntityType();
        List<ItemStack> loots = event.getDrops();
        switch (entity) {
            case SPIDER:
            case CAVE_SPIDER:
                if (UhcHost.getRANDOM().nextInt(100) + 1 <= getLoot(Material.STRING))
                    loots.add(new ItemStack(Material.STRING));
                break;
            case ENDERMAN:
                if (UhcHost.getRANDOM().nextInt(100) + 1 <= getLoot(Material.ENDER_PEARL))
                    loots.add(new ItemStack(Material.ENDER_PEARL));
        }
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        ItemStack is = event.getEntity().getItemStack();
        if (is == null)
            return;
        ItemStack replace = null;
        if (is.getType() == Material.GRAVEL) {
            if ((new Random()).nextInt(100) + 1 <= getLoot(Material.FLINT))
                replace = new ItemStack(Material.FLINT);
        }
        if (replace != null)
            event.getEntity().setItemStack(replace);
    }

    public static LootsRules getInstance() {
        return (instance == null) ? (instance = new LootsRules()) : instance;
    }

}
