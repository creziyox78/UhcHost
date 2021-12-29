package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.kakashi.SharinganGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SharinganItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public SharinganItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cSharingan");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Kakashi) {
                    Kakashi kakashi = (Kakashi) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        new SharinganGUI(main, kakashi).open(player);
                    }
                }
            }
        });
    }
}
