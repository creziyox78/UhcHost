package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.*;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    //Soul Society
    YAMAMOTO(Yamamoto.class), //TODO FIX NO ABSO
    SOIFON(SoiFon.class), //TODO ITEMS
    OMAEDA(Omaeda.class), //FINISHED
    KIRA(Kira.class), //FINISHED
    UNOHANA(Unohana.class), //FINISHED
    ISANE(Isane.class), //FINISHED
    HINAMORI(Hinamori.class), //FINISHED
    BYAKUYA(Byakuya.class), //TODO
    
    ;

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
