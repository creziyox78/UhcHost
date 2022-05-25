package fr.lastril.uhchost.test.utils;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.test.packet.PacketSender;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EntityUtil {
    private final UhcHost plugin;

    private final Random r = UhcHost.getRANDOM();

    private final Map<Player, Set<EntityArmorStand>> fakeArmorStandsMap = new HashMap<>();

    public EntityUtil(UhcHost plugin) {
        this.plugin = plugin;
    }

    public void sendBlizzard(Player player, Location loc) {
        Set<EntityArmorStand> fakeArmorStands = this.fakeArmorStandsMap.computeIfAbsent(player, k -> new HashSet<>());
        EntityArmorStand as = new EntityArmorStand(((CraftWorld)player.getWorld()).getHandle());
        as.setInvisible(true);
        as.setSmall(true);
        as.setGravity(false);
        as.setArms(true);
        as.setHeadPose(new Vector3f(this.r.nextInt(360), this.r.nextInt(360), this.r.nextInt(360)));
        as.setLocation(loc.getX() + MathUtils.randomDouble(-1.5D, 1.5D), loc.getY() - 0.5, loc.getZ() + MathUtils.randomDouble(-1.5D, 1.5D), 0.0F, 0.0F);
        //as.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
        fakeArmorStands.add(as);
        for (Player players : player.getWorld().getPlayers()) {
            PacketSender.send(players, new PacketPlayOutSpawnEntityLiving(as));
            PacketSender.send(players, new PacketPlayOutEntityEquipment(as.getId(), 4, CraftItemStack.asNMSCopy(new ItemStack(Material.PACKED_ICE))));
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player pl : player.getWorld().getPlayers()) {
                if (as == null)
                    continue;
                PacketSender.send(pl, new PacketPlayOutEntityDestroy(as.getId()));

            }
            fakeArmorStands.remove(as);
        },40L);
    }

    public void clearBlizzard(Player player) {
        if (!this.fakeArmorStandsMap.containsKey(player))
            return;
        for (EntityArmorStand as : this.fakeArmorStandsMap.get(player)) {
            for (Player pl : player.getWorld().getPlayers()) {
                PacketSender.send(pl, new PacketPlayOutEntityDestroy(as.getId()));
            }
        }
        this.fakeArmorStandsMap.remove(player);
    }

}
