package fr.lastril.uhchost.modes.naruto.v2.pathfinder.pakkun;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathEntity;
import net.minecraft.server.v1_8_R3.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class PathFinderWalkToPlayer extends PathfinderGoal {

    private final EntityInsentient entity;
    private final UUID p;
    private PathEntity path;

    public PathFinderWalkToPlayer(EntityInsentient entitycreature, UUID p) {
        this.entity = entitycreature;
        this.p = p;
    }

    @Override
    public boolean a() {
        if (Bukkit.getPlayer(p) == null) {
            return path != null;
        }
        Location targetLocation = Bukkit.getPlayer(p).getLocation();
        this.path = this.entity.getNavigation().a(targetLocation.getX() + 1, targetLocation.getY(), targetLocation.getZ() + 1);
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
