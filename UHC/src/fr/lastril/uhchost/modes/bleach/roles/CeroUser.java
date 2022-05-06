package fr.lastril.uhchost.modes.bleach.roles;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import org.bukkit.Color;

import java.util.List;

public interface CeroUser {

    boolean canUseCero(CeroType ceroType);

    void onUseCero(CeroType ceroType);

    int getCeroRedValue();
    int getCeroGreenValue();
    int getCeroBlueValue();

    List<AbstractCero> getCero();

}
