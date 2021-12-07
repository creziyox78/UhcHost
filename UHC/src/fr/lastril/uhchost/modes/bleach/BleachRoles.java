package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.modes.bleach.roles.soulsociety.Yamamoto;
import fr.lastril.uhchost.modes.roles.Role;

public enum BleachRoles {

    YAMAMOTO(Yamamoto.class);

    private final Class<? extends Role> role;

    BleachRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }

}
