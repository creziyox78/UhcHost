package fr.lastril.uhchost.modes.naruto.v2.pathfinder.c2;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Wolf;

import java.lang.reflect.Field;
import java.util.UUID;

public class C2Invoker {

    private static Field gsa;
    private static Field goalSelector;
    private static Field targetSelector;

    static {
        try {
            gsa = PathfinderGoalSelector.class.getDeclaredField("b");
            gsa.setAccessible(true);
            goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
            goalSelector.setAccessible(true);
            targetSelector = EntityInsentient.class.getDeclaredField("targetSelector");
            targetSelector.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void invokeC2(Location location, UUID toExplode) {
        try {
            World world = location.getWorld();
            Wolf wolf = world.spawn(location, Wolf.class);
            EntityLiving nmsEntity = ((CraftLivingEntity) wolf).getHandle();
            if (nmsEntity instanceof EntityInsentient) {
                PathfinderGoalSelector goal = (PathfinderGoalSelector) goalSelector.get(nmsEntity);
                PathfinderGoalSelector target = (PathfinderGoalSelector) targetSelector.get(nmsEntity);
                gsa.set(goal, new UnsafeList<Object>());
                gsa.set(target, new UnsafeList<Object>());
                goal.a(0, new PathfinderGoalFloat((EntityInsentient) nmsEntity));
                goal.a(1, new PathFinderExploseToPlayer((EntityInsentient) nmsEntity, toExplode));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
