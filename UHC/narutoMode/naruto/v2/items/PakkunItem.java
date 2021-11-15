package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.kakashi.PakkunGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PakkunItem extends QuickItem {

    public PakkunItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Pakkun");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kakashi) {
                    Kakashi kakashi = (Kakashi) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownPakkun() == 0) {
                            new PakkunGUI(main).open(player);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownPakkun()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Kakashi"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
