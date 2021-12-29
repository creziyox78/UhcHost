package fr.lastril.uhchost.modes.naruto.v2.sakonukon;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SakonUkonManager implements Listener {

    private final NarutoV2Manager manager;

    private final Map<String, UUID> powersWaits;

    private final List<Location> unbreakableCageBlocks;

    private boolean hasUsedCage;

    public SakonUkonManager(NarutoV2Manager manager) {
        this.manager = manager;
        this.powersWaits = new HashMap<>();
        this.unbreakableCageBlocks = new ArrayList<>();
        Bukkit.getPluginManager().registerEvents(this, UhcHost.getInstance());
    }

    public Map<String, UUID> getPowersWaits() {
        return powersWaits;
    }

    public List<Location> getUnbreakableCageBlocks() {
        return unbreakableCageBlocks;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDirtBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location locBlock = block.getLocation();
        if (this.unbreakableCageBlocks.contains(locBlock)) {
            event.setCancelled(true);
        }
    }

    public void addPowerWait(String key, Player player, String otherwise) {
        this.powersWaits.put(key, player.getUniqueId());
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous devez attendre 15 secondes, que "+otherwise+" utilise aussi son pouvoir.");
        new BukkitRunnable(){

            @Override
            public void run() {
                if(SakonUkonManager.this.powersWaits.containsKey(key)){
                    SakonUkonManager.this.powersWaits.remove(key);
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Votre pouvoir a expiré.");
                }
            }
        }.runTaskLater(UhcHost.getInstance(), 20*15);
    }

    public boolean hasUsedCage() {
        return hasUsedCage;
    }

    public void setHasUsedCage(boolean hasUsedCage) {
        this.hasUsedCage = hasUsedCage;
    }
}
