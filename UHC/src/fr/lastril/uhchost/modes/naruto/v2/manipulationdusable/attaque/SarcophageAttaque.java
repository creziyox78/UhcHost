package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque;

import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.raytracing.BoundingBox;
import fr.lastril.uhchost.tools.API.raytracing.RayTrace;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;

public class SarcophageAttaque extends SandShape {

    public SarcophageAttaque() {
        super("Sarcophage de sable", ShapeType.ATTAQUE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {

        Player target = this.getTargetPlayer(player, 15);
        if(target != null){
            Location min = target.getLocation().clone().subtract(2, 1, 2), max = target.getLocation().clone().add(2, 5, 2);

            Cuboid sarcophage = new Cuboid(min, max);
            sarcophage.getBlocks().forEach(block -> block.setType(Material.SAND));
            return true;
        }
        return false;
    }

    @Override
    public int getSandPrice() {
        return 45;
    }

    public Player getTargetPlayer(Player player, double distanceMax) {

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
