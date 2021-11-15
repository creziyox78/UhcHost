package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.gui.sai.ToileGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Sai;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ToileItem extends QuickItem {


    public ToileItem(UhcHost main, Sai sai) {
        super(Material.NETHER_STAR);
        super.setName("§6Toile aux Monstres Fantomatiques");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Sai) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        new ToileGUI(main, sai).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Saï"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
