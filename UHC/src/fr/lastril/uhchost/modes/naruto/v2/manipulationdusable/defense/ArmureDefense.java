package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.defense;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.SandShape;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ArmureDefense extends SandShape {

    public ArmureDefense() {
        super("Armure du sable", ShapeType.DEFENSE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {
        if(gaara.isInArmure()){
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes déjà sous l'effet de votre armure de sable.");
            return false;
        } else {
            gaara.setInArmure(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
        }
        return true;
    }

    @Override
    public int getSandPrice() {
        return 32;
    }
}
