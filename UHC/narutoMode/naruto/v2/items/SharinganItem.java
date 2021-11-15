package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.naruto.v2.gui.kakashi.SharinganGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SharinganItem extends QuickItem {

    public SharinganItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cSharingan");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kakashi) {
                    Kakashi kakashi = (Kakashi) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        new SharinganGUI(main, kakashi).open(player);
                    }
                }
            }
        });
    }
}
