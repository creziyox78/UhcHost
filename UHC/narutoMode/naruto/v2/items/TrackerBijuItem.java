package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.Biju;
import fr.maygo.uhc.modes.naruto.v2.gui.TrackerBijuGUI;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Madara;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class TrackerBijuItem extends QuickItem {

    private final int index = 0;

    private Biju bijuTracked;

    public TrackerBijuItem(UhcHost main) {
        super(Material.COMPASS);
        super.setName("§6Tracker de Bijû");
        super.setLore();
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_BLOCK || onClick.getAction() == Action.RIGHT_CLICK_AIR) {
                if (PlayerManager.hasRole()) {
                    if (PlayerManager.getRole() instanceof Obito) {
                        Obito obito = (Obito) PlayerManager.getRole();
                        new TrackerBijuGUI(obito, null, main).open(player);
                    } else if (PlayerManager.getRole() instanceof Madara) {
                        Madara madara = (Madara) PlayerManager.getRole();
                        new TrackerBijuGUI(null, madara, main).open(player);
                    } else {
                        player.sendMessage(Messages.not("Obito ou Madara"));
                    }
                }
            }

        });
    }
}
