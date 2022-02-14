package fr.lastril.uhchost.modes.ds;

import fr.lastril.uhchost.modes.ds.roles.demon.Enmu;
import fr.lastril.uhchost.modes.lg.roles.lg.*;
import fr.lastril.uhchost.modes.lg.roles.solo.*;
import fr.lastril.uhchost.modes.lg.roles.village.*;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.Voyante;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.VoyanteBavarde;
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
