package fr.lastril.uhchost.modes.naruto.v2.biju;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Biju extends BukkitRunnable implements Listener {

    private final String world = "game";

    private final UhcHost main;

    private final Biju biju;
    private final BijuManager bijuManager = UhcHost.getInstance().getNarutoV2Manager().getBijuManager();
    private int firstSpawn;

    public Biju(UhcHost main) {
        biju = this;
        this.main = main;
    }

    public void sendToStaffSpec(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if (!playerManager.isAlive() && player.isOp()) {
                player.sendMessage(message);
            }
        }
    }

    protected void setHote(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (main.getNarutoV2Manager().getBijuManager().getHotesBiju().get(this.getClass()) == null && !main.getNarutoV2Manager().getBijuManager().isAlreadyHote(playerManager)) {
            main.getNarutoV2Manager().getBijuManager().getHotesBiju().put(this.getClass(), main.getPlayerManager(player.getUniqueId()));
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes devenu l'hôte de " + getItem().getName() + ".");
            sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage() + player.getName() + " est devenue l'hôte de " + getItem().getName() + ".");
        }
    }

    public Biju getBiju() {
        return biju;
    }

    public abstract QuickItem getItem();

    public void setupBiju(String nameBiju) {
        for (PlayerManager playerManager : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithRole(Ino.class)) {
            if (playerManager.getPlayer() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        playerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                + nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
                    }
                }.runTaskLater(UhcHost.getInstance(), 20 * 60 * 2);
            }
        }
        sendToStaffSpec(Messages.PREFIX_SPEC_STAFF.getMessage()
                + nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
        for (PlayerManager playerManager : UhcHost.getInstance().getNarutoV2Manager().getPlayerManagersWithRole(Obito.class)) {
            if (playerManager.getPlayer() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        playerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                + nameBiju + "§e est en x:" + getSafeSpawnLocation().getX() + " ,y:" + getSafeSpawnLocation().getY() + " ,z:" + getSafeSpawnLocation().getZ() + " .");
                    }
                }.runTaskLater(UhcHost.getInstance(), 10);
            }
        }
    }

    @Deprecated
    public abstract Location getSpawnLocation();

    public Location getSafeSpawnLocation() {
        return new Location(getSpawnLocation().getWorld(), getSpawnLocation().getX(), getSpawnLocation().getWorld().getHighestBlockYAt(getSpawnLocation()) + 5, getSpawnLocation().getZ());
    }

    public boolean itemInInventory(ItemStack item) {
        if (bijuManager.isCraftedJubi())
            return true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (item != null && item.getType() == Material.NETHER_STAR) {
                if (player.getInventory().contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract Entity getBijuEntity();

    public int getFirstSpawn() {
        return firstSpawn;
    }

    public void setFirstSpawn(int firstSpawn) {
        this.firstSpawn = firstSpawn;
    }

    public String getWorld() {
        return world;
    }
}
