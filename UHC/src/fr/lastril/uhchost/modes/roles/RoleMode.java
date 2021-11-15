package fr.lastril.uhchost.modes.roles;

import fr.lastril.uhchost.UhcHost;

import java.util.List;

public interface RoleMode<T extends CustomRoleClass> {

    void giveRoles();

    @Deprecated
    default List<Class<? extends Role>> getCompo() {
        return UhcHost.getInstance().getGamemanager().getComposition();
    }

    List<Role> getRoles();

    String getDocLink();

}
