package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.*;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    //Soul Society
    YAMAMOTO(Yamamoto.class), //FINISHED
    SOIFON(SoiFon.class), //TODO ITEMS
    OMAEDA(Omaeda.class), //FINISHED
    KIRA(Kira.class), //TODO BE TESTED
    UNOHANA(Unohana.class), //TODO BE TESTED
    ISANE(Isane.class), //TODO BE TESTED
    HINAMORI(Hinamori.class),
    ;

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
