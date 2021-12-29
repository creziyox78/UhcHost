package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque;


import fr.lastril.uhchost.modes.naruto.v2.items.swords.GaaraLanceSword;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.entity.Player;

public class LanceAttaque extends SandShape {

    public LanceAttaque() {
        super("Lance", ShapeType.ATTAQUE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {
        main.getInventoryUtils().giveItemSafely(player, new GaaraLanceSword().toItemStack());
        return true;
    }

    @Override
    public int getSandPrice() {
        return 64;
    }

}
