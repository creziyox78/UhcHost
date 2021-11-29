package fr.lastril.uhchost.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ModeManager {

    public boolean compositionHide;

    public void sendRoleToKnow(){
        try {
            for (PlayerManager playerManager : UhcHost.getInstance().getPlayerManagerAlives()) {
                if (playerManager.getPlayer() != null) {
                    Player player = playerManager.getPlayer();
                    player.setHealth(player.getMaxHealth());
                    if (playerManager.getRole() != null) {
                        playerManager.getRole().afterRoles(player);
                        if (!playerManager.getRole().getRoleToKnow().isEmpty()) {
                            for (Class<? extends Role> roleToKnow : playerManager.getRole().getRoleToKnow()) {
                                if(!getPlayerManagersWithRole(roleToKnow).isEmpty()){
                                    player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§eLe(s) joueur(s) possédant le rôle §6" + roleToKnow.newInstance().getRoleName() + "§e sont :");
                                    for (PlayerManager PlayerManagerHasRole : getPlayerManagersWithRole(roleToKnow)) {
                                        player.sendMessage("§6- " + PlayerManagerHasRole.getPlayerName());
                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

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
