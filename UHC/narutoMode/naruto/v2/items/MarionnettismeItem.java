package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.SasoriGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Sasori;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MarionnettismeItem extends QuickItem {

    public MarionnettismeItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cMarionnettisme");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Sasori) {
                    Sasori sasori = (Sasori) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        new SasoriGUI(main, sasori, PlayerManager.getKills()).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Sasori"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
