package fr.lastril.uhchost.modes.bleach.ceros;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCero {

    protected final UhcHost main = UhcHost.getInstance();
    private final String name;
    private final List<String> lore;
    private final CeroType type;
    private final int speed;
    private final byte color;

    protected AbstractCero(String name, CeroType type, int speed, byte color) {
        this.name = name;
        this.type = type;
        this.speed = speed;
        this.color = color;
        this.lore = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public CeroType getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }

    public void addLore(String lore) {
        this.lore.add(lore);
    }

    public abstract void action(QuickEvent event);

    public byte getColor() {
        return color;
    }

    public QuickItem getItem() {
        return new QuickItem(Material.INK_SACK, 1, color)
                .setName(name)
                .glow(true)
                .setLore(lore)
                .onClick(onClick -> {
                    Player player = onClick.getPlayer();
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                    if(playerManager.hasRole() && playerManager.getRole() instanceof CeroUser) {
                        CeroUser ceroUser = (CeroUser) playerManager.getRole();
                        if(ceroUser.canUseCero() && bleachPlayerManager.canUsePower()) {
                            UhcHost.debug("Cero used by " + player.getName());
                            action(onClick);
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§c§lVous ne pouvez pas votre Cero pour le moment !");
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§c§lVous ne pouvez pas utiliser de Cero !");
                    }
                });
    }

    public void giveCero(Player player) {
        main.getInventoryUtils().giveItemSafely(player, getItem().toItemStack());
    }

    protected Vector getRightHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    protected Vector getLeftHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

    public void spawnLaser(Location basis, Location target, int red, int green, int blue) {
        World world = basis.getWorld();
        if(world == target.getWorld()) {
            double dis = basis.distance(target);
            Vector pos1 = basis.toVector();
            Vector pos2 = target.toVector();

            Vector vector = pos2.clone().subtract(pos1).normalize().multiply(0.1);
            double cover = 0;
            for(; cover < dis; pos1.add(vector)) {
                WorldUtils.spawnColoredParticle(pos1.toLocation(basis.getWorld()), EnumParticle.REDSTONE, red, green, blue);
                cover += 0.1;
            }
        }
    }


    protected boolean needExplosion(Location startLocation, Location location, Player user) {
        for(Entity entity : location.getWorld().getNearbyEntities(location, 20, 20, 20)) {
            if(entity instanceof Player) {
                Player player = (Player) entity;
                if(player != user) {
                    /*if(location.distance(player.getLocation().add(0, 0.2, 0)) < 1.5) {
                        return true;
                    }*/
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    if(playerManager.isAlive()) {
                        if(location.distance(player.getLocation().add(0, 0.2, 0)) < 1.5) {
                            return true;
                        }
                    }
                }

            }
        }
        if(!location.getBlock().isEmpty() || startLocation.distance(location) >= 30) {
            return true;
        }
        return false;
    }

}
