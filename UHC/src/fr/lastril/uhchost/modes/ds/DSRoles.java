package fr.lastril.uhchost.modes.ds;

import fr.lastril.uhchost.modes.ds.roles.demon.Enmu;
import fr.lastril.uhchost.modes.roles.Role;

public enum DSRoles {


    /*
     * CAMP DEMONS
     */
    ENMU(Enmu.class),
    ;

    private final Class<? extends Role> role;

    DSRoles(Class<? extends Role> role) {
        this.role = role;
    }

    public Class<? extends Role> getRole() {
        return role;
    }
}
