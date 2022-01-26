package fr.lastril.uhchost.modes.naruto.v2.pathfinder.kokuo;

import com.google.common.base.Predicates;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.PlayerManager;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.Collections;
import java.util.List;

public class PathFinderAttackPlayer extends PathfinderGoalNearestAttackableTarget {
    public PathFinderAttackPlayer(EntityCreature  entitycreature, Class oclass) {
        super(entitycreature, oclass, false);
    }

    @Override
    public boolean a() {
        final double d0 = this.f();
        final List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), Predicates.and(this.c, IEntitySelector.d));
        Collections.sort(list, this.b);
        if(list.isEmpty())
            return false;
        else {
            this.d = (EntityLiving) list.get(0);
            if(this.d instanceof EntityHuman) {
                final EntityHuman eh = (EntityHuman)list.get(0);
                PlayerManager joueur = UhcHost.getInstance().getPlayerManager(eh.getBukkitEntity().getUniqueId());
                if(eh.getBukkitEntity().getWorld() == this.e.getBukkitEntity().getWorld()){
                    if(eh.getBukkitEntity().getLocation().distance(this.e.getBukkitEntity().getLocation()) < 8.0D && eh.getBukkitEntity().getGameMode() != GameMode.SPECTATOR && joueur.isAlive()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void c() {
        this.e.setGoalTarget(this.d, EntityTargetEvent.TargetReason.CLOSEST_PLAYER, true);
        this.d.damageEntity(DamageSource.mobAttack(this.e), (float)0.75);
    };
}
