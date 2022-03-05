package fr.lastril.uhchost.modes.roles;

import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;

import java.util.List;

public interface RoleAnnounceMode {

    int getRoleAnnouncement();

    void setRoleAnnouncement(int roleAnnouncement);

    default boolean isRoleAnnonced() {
        return getRoleAnnouncement() <= 0;
    }

    List<Camps> getCamps();

    IQuickInventory getCurrentCompoGui();

}
