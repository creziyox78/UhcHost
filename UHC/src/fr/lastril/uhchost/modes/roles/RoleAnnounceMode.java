package fr.lastril.uhchost.modes.roles;

public interface RoleAnnounceMode {

    int getRoleAnnouncement();

    void setRoleAnnouncement(int roleAnnouncement);

    default boolean isRoleAnnonced(int roleAnnouncement) {
        return roleAnnouncement >= getRoleAnnouncement();
    }

}
