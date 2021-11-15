package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MarqueMauditeItem extends QuickItem {

    public MarqueMauditeItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dMarque Maudite");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof MarqueMauditeUser) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownMarqueMaudite() <= 0) {
                            MarqueMauditeUser marqueMauditeUser = (MarqueMauditeUser) PlayerManager.getRole();
                            marqueMauditeUser.onUseMarqueMaudite(main, player, PlayerManager);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownMarqueMaudite()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Tayuya, Jirobo, Kidomaru, Jugo ou Kimimaro"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface MarqueMauditeUser {

        void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager PlayerManager);

    }

}
