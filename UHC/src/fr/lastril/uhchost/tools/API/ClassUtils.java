package fr.lastril.uhchost.tools.API;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.raytracing.BoundingBox;
import fr.lastril.uhchost.tools.API.raytracing.RayTrace;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassUtils {

    public static String getDirectionOf(Location ploc, Location to) {
        if(ploc == null || to == null) return "?";
        if(ploc.getWorld() != to.getWorld()) return "?";
        ploc.setY(0.0D);
        to.setY(0.0D);

        String[] arrows = {"⬆", "⬈", "➡", "⬊", "⬇", "⬋", "⬅", "⬉", "⬆"};
        Vector d = ploc.getDirection();

        Vector v = to.subtract(ploc).toVector().normalize();

        double a = Math.toDegrees(Math.atan2(d.getX(), d.getZ()));
        a -= Math.toDegrees(Math.atan2(v.getX(), v.getZ()));

        a = ((int)(a + 22.5D) % 360);

        if (a < 0.0D) {
            a += 360.0D;
        }

		/*String color = "§4";
		if(ploc.distance(to) > 750 && ploc.distance(to) < 1000) {
			color = "§c";
		}else if(ploc.distance(to) > 500 && ploc.distance(to) < 750) {
			color = "§6";
		}else if(ploc.distance(to) > 250 && ploc.distance(to) < 500) {
			color = "§e";
		}else if(ploc.distance(to) < 250) {
			color = "§a";
		}*/
        return arrows[((int) a / 45)];
    }

    public static void hidePlayerWithArmor(Player player, boolean hideItem, int time, boolean showWhenEnd){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(UhcHost.getInstance(), () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
            PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, null);
            PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, null);
            PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, null);
            PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, null);
            PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, null);
            for(PlayerManager target: UhcHost.getInstance().getPlayerManagerOnlines()){
                Player reciever = target.getPlayer();
                if(reciever != null && reciever != player){
                    if(hideItem){
                        ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(handPacket);
                    }
                    ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                    ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(chestPacket);
                    ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(legPacket);
                    ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(bootsPacket);
                }
            }
        },0,1);
        Bukkit.getScheduler().runTaskLater(UhcHost.getInstance(), () -> {
            task.cancel();
            if(showWhenEnd){
                showPlayer(player);
            }
        }, 20L *time);

    }

    public static void showPlayer(Player player){
        PacketPlayOutEntityEquipment handPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, CraftItemStack.asNMSCopy(player.getItemInHand()));
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));

        if(player.hasPotionEffect(PotionEffectType.INVISIBILITY))
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
        for(PlayerManager target: UhcHost.getInstance().getPlayerManagerOnlines()){
            Player reciever = target.getPlayer();
            if(reciever != null && reciever != player){
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(handPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(helmetPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(chestPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(legPacket);
                ((CraftPlayer)reciever).getHandle().playerConnection.sendPacket(bootsPacket);
            }
        }
    }

    public static List<Location> getCircle(Location center, double radius, int points){
        List<Location> locs = new ArrayList<>();

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            Location point = center.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
            locs.add(point);
        }
        return locs;
    }

    public static void ripulseEntityFromLocation(Location location, int distance,int powerMultiply, int powerHigh){
        for(Entity entity : location.getWorld().getNearbyEntities(location,distance, distance, distance)){
            Location initialLocation = location.clone();
            initialLocation.setPitch(0.0f);
            Vector origin = initialLocation.toVector();
            Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(origin);
            fromPlayerToTarget.multiply(powerMultiply); //6
            fromPlayerToTarget.setY(powerHigh); // 2
            entity.setVelocity(fromPlayerToTarget);
        }
    }

    public static void pullEntityToLocation(Entity e, Location loc) {
        Location entityLoc = e.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);
        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0D + 0.07D * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + 0.07D * t) * (loc.getZ() - entityLoc.getZ()) / t;
        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
    }

    public static void setCorrectHealth(Player player, double health, boolean kill){
        if(health <= 0){
            if(kill){
                player.damage(100);
            } else {
                player.setHealth(0.5);
            }
        } else if(health >= player.getMaxHealth()){
            player.setHealth(player.getMaxHealth());
        } else {
            player.setHealth(health);
        }
    }

    public static boolean getLookingAt(Player player, LivingEntity livingEntity){
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }

    public static Player getTargetPlayer(Player player, double distanceMax) {
        RayTrace rayTrace = new RayTrace(player.getEyeLocation().toVector(), player.getEyeLocation().getDirection());
        List<Vector> positions = rayTrace.traverse(distanceMax, 0.15D);
        for (int i = 0; i < positions.size(); i++) {
            Location position = positions.get(i).toLocation(player.getWorld());
            Collection<Entity> entities = player.getWorld().getNearbyEntities(position, 1.0D, 1.0D, 1.0D);
            for (Entity entity : entities) {
                if (entity instanceof Player && entity != player && rayTrace.intersects(new BoundingBox(entity), distanceMax, 0.15D))
                    return (Player) entity;
            }
        }
        return null;
    }

}
