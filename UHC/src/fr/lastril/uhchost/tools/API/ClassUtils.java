package fr.lastril.uhchost.tools.API;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Particle;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.raytracing.BoundingBox;
import fr.lastril.uhchost.tools.API.raytracing.RayTrace;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

import static fr.lastril.uhchost.world.WorldUtils.generateSphere;

public class ClassUtils {

    public static List<Location> getNewSphere(Location centerBlock, int radius, boolean hollow) {
        List<Location> circleBlocks = new ArrayList<>();
        int bX = centerBlock.getBlockX();
        int bY = centerBlock.getBlockY();
        int bZ = centerBlock.getBlockZ();
        for (int x = bX - radius; x <= bX + radius; x++) {
            for (int y = bY - radius; y <= bY + radius; y++) {
                for (int z = bZ - radius; z <= bZ + radius; z++) {
                    double distance = ((bX - x) * (bX - x) + (bZ - z) * (bZ - z) + (bY - y) * (bY - y));
                    if (distance < (radius * radius) && (!hollow || distance >= ((radius - 1) * (radius - 1)))) {
                        Location block = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(block);
                    }
                }
            }
        }
        return circleBlocks;
    }
    public static String getDirectionOf(Location ploc, Location to) {
        if(ploc == null || to == null) return "?";
        if(ploc.getWorld() != to.getWorld()) return "? (Les mondes ont différents)";
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

    public static void fakeExplosion(Location location, int power){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float)location.getX(), (float)location.getY(), (float)location.getZ(), 0.0f, 0.0f, 0.0f, 0, power);
        for(Player p : location.getWorld().getPlayers()){
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
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
        PacketPlayOutEntityEquipment helmetPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(player.getInventory().getHelmet()));
        PacketPlayOutEntityEquipment chestPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(player.getInventory().getChestplate()));
        PacketPlayOutEntityEquipment legPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(player.getInventory().getLeggings()));
        PacketPlayOutEntityEquipment bootsPacket = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(player.getInventory().getBoots()));

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
            ripulseSpecificEntityFromLocation(entity, location,powerMultiply, powerHigh);
        }
    }

    public static void switchPlayersLocation(Player player1, Player player2){
        Location location1 = player1.getLocation();
        Location location2 = player2.getLocation();
        player1.teleport(location2);
        player2.teleport(location1);
    }

    public static void ripulseSpecificEntityFromLocation(Entity entity, Location location, int powerMultiply, int powerHigh){
        Location initialLocation = location.clone();
        initialLocation.setPitch(0.0f);
        Vector origin = initialLocation.toVector();
        Vector fromPlayerToTarget = entity.getLocation().toVector().clone().subtract(origin);
        fromPlayerToTarget.multiply(powerMultiply); //6
        fromPlayerToTarget.setY(powerHigh); // 2
        entity.setVelocity(fromPlayerToTarget);
    }
    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }


    public static void packetMobForPlayers(Player player){
        for(Player players : Bukkit.getOnlinePlayers()){
            PacketPlayOutSpawnEntityLiving mobPacket = new PacketPlayOutSpawnEntityLiving(new EntityOcelot(getWorldNMS(player.getWorld())));
            ((CraftPlayer)players).getHandle().playerConnection.sendPacket(mobPacket);
        }
    }

    public static net.minecraft.server.v1_8_R3.WorldServer getWorldNMS(World world){
        return ((CraftWorld)world).getHandle();
    }

    public static void pullEntityToLocation(Entity e, Location loc, double powerX, double powerY, double powerZ) {
        Location entityLoc = e.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);
        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0D + powerX * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + powerY * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + powerZ * t) * (loc.getZ() - entityLoc.getZ()) / t;
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

    public static void changeSlotItemRandomlyInInventory(Player player, ItemStack itemStack){
        if(itemStack != null){
            Inventory inventory = player.getInventory();
            int slot = inventory.firstEmpty();
            if(slot != -1){
                int slots = getSlotItemInInventory(player, itemStack);
                if(slots != -1){
                    inventory.setItem(getSlotItemInInventory(player, itemStack), null);
                }
                inventory.setItem(slot, itemStack);
            }
        }

    }

    public static int getSlotItemInInventory(Player player, ItemStack itemStack){
        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) != null && inventory.getItem(i).isSimilar(itemStack)){
                return i;
            }
        }
        return -1;
    }

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static List<Location> getSphere(Location location, double radiusX, double radiusY, double radiusZ, boolean filled){
        Vector pos = location.toVector();
        World world = location.getWorld();
        List<Location> blocks = new ArrayList<>();

        radiusX += 0.5;
        radiusY += 0.5;
        radiusZ += 0.5;

        final double invRadiusX = 1 / radiusX;
        final double invRadiusY = 1 / radiusY;
        final double invRadiusZ = 1 / radiusZ;

        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusY = (int) Math.ceil(radiusY);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);

        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    if (!filled) {
                        if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }

                    blocks.add(pos.add(new Vector(x,y,z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x,y,z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x,-y,z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x,y,-z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x,-y,z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x,-y,-z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x,y,-z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x,-y,-z)).toLocation(world));
                }
            }
        }

        return blocks;
    }

    private static final double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    private static final double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    public static Vector getRightHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    public static Vector getLeftHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

}
