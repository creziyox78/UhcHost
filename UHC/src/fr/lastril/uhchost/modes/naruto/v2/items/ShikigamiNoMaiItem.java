package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.shikigaminomai.ShikigamiNoMaiGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Konan;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ShikigamiNoMaiItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public ShikigamiNoMaiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cShikigami no Mai");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Konan) {
                    Konan konan = (Konan) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        new ShikigamiNoMaiGUI(main, konan).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Konan"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
