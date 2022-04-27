package fr.lastril.uhchost.modes.bleach.roles;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import org.bukkit.Color;

public interface CeroUser {

    boolean canUseCero();

    void onUseCero();

    int getCeroRedValue();
    int getCeroGreenValue();
    int getCeroBlueValue();

    AbstractCero getCero();

}
