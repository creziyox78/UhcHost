package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.soulsociety.*;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    //Soul Society
    YAMAMOTO(Yamamoto.class),
    SOIFON(SoiFon.class),
    OMAEDA(Omaeda.class),
    KIRA(Kira.class),
    UNOHANA(Unohana.class),


    ;

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
