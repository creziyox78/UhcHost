package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.edotensei.EdoTenseiGUI;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EdoTenseiItem extends QuickItem {

    public EdoTenseiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§5Edo Tensei");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof EdoTenseiUser) {
                    EdoTenseiUser user = (EdoTenseiUser) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        new EdoTenseiGUI(main, user).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Orochimaru ou Kabuto"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface EdoTenseiUser {

    }
}
