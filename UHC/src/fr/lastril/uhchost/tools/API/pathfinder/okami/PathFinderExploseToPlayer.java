package fr.lastril.uhchost.tools.API.pathfinder.okami;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class PathFinderExploseToPlayer extends PathfinderGoal {

    private EntityInsentient entity;
    private PathEntity path;
    private UUID p;

    public PathFinderExploseToPlayer(EntityInsentient entitycreature, UUID p) {
        this.entity = entitycreature;
        this.p = p;
    }

    @Override
    public boolean a() {
        if (Bukkit.getPlayer(p) == null) {
            return path != null;
        }
        Location targetLocation = Bukkit.getPlayer(p).getLocation();
        if(targetLocation.distance(entity.getBukkitEntity().getLocation()) < 1){
            entity.die();
            targetLocation.getWorld().createExplosion(targetLocation, 3.0f);
        }
        this.path = this.entity.getNavigation().a(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
        if (this.path != null) {
            this.c();
        }
        return this.path != null;
    }

    @Override
    public void c() {
        this.entity.getNavigation().a(this.path, 1D);
    }
}
