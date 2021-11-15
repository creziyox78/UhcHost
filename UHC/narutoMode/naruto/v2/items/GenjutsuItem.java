package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.genjutsu.GenjutsuGUI;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GenjutsuItem extends QuickItem {

    public GenjutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cGenjutsu");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof GenjutsuUser) {
                    GenjutsuUser genjutsuUser = (GenjutsuUser) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        new GenjutsuGUI(main, genjutsuUser).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Sasuke, Obito ou Itachi"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface GenjutsuUser {

        void useTsukuyomi();

        int getTsukuyomiUses();

        void useIzanami();

        boolean hasUsedIzanami();
    }

}
