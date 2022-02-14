package fr.lastril.uhchost.modes.ds.roles;

import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.CustomRoleClass;

public interface DSRole extends CustomRoleClass {
    Camps getCamp();
}
