package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque;

import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.tools.API.ClassUtils;
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

        Player target = ClassUtils.getTargetPlayer(player, 15);
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
}
