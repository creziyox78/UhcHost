package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.defense;

import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.tools.API.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BouclierDefense extends SandShape {

    public BouclierDefense() {
        super("Bouclier du sable", ShapeType.DEFENSE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {
        if(player != null){
            Location min = player.getLocation().clone().subtract(2, 1, 2), max = player.getLocation().clone().add(2, 5, 2);
            Cuboid sarcophage = new Cuboid(min, max);
            sarcophage.getBlocks().forEach(block -> {
                block.setType(Material.SAND);
                gaara.getBouclierBlock().add(block);
            });
            sarcophage.getCenter().add(0 , 1, 0).getBlock().setType(Material.AIR);
            sarcophage.getCenter().getBlock().setType(Material.AIR);
        }
        return true;
    }

    @Override
    public int getSandPrice() {
        return 64;
    }
}
