package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MarqueMauditeItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public MarqueMauditeItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dMarque Maudite");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof MarqueMauditeUser) {
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownMarqueMaudite() <= 0) {
                            MarqueMauditeUser marqueMauditeUser = (MarqueMauditeUser) joueur.getRole();
                            marqueMauditeUser.onUseMarqueMaudite(main, player, joueur);
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownMarqueMaudite()));
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

        void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager joueur);

    }

}
