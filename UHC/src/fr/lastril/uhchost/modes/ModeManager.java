package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ModeManager {

    public List<PlayerManager> getPlayerManagersWithCamps(Camps camp) {
        return UhcHost.getInstance().getAllPlayerManager().values().stream()
                .filter(PlayerManager::hasRole)
                .filter(PlayerManager -> Objects.equals(PlayerManager.getRole().getCamp(), camp))
                .collect(Collectors.toList());
    }

    public List<PlayerManager> getPlayerManagersWithRole(Class<? extends Role> role) {
        return UhcHost.getInstance().getAllPlayerManager().values().stream()
                .filter(PlayerManager::hasRole)
                .filter(PlayerManager -> PlayerManager.getRole().getClass().getSimpleName().equalsIgnoreCase(role.getSimpleName()))
                .collect(Collectors.toList());
    }

    public List<PlayerManager> getPlayerManagersNotInCamps(Camps camp) {
        return UhcHost.getInstance().getAllPlayerManager().values().stream()
                .filter(PlayerManager::hasRole)
                .filter(PlayerManager -> !Objects.equals(PlayerManager.getRole().getCamp(), camp))
                .collect(Collectors.toList());
    }

    public boolean isCancelDamage(EntityDamageByEntityEvent event) {
        return false;
    }

}
