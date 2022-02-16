package fr.lastril.uhchost.modes.roles;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;

import java.util.List;

public interface RoleAnnounceMode {

    int getRoleAnnouncement();

    void setRoleAnnouncement(int roleAnnouncement);

    default boolean isRoleAnnonced(int roleAnnouncement) {
        return roleAnnouncement >= getRoleAnnouncement();
    }

    List<Camps> getCamps();

    IQuickInventory getCurrentCompoGui();

}
