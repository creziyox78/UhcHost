package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.InoGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InoItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public InoItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Ino");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Ino) {
                    Ino ino = (Ino) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        new InoGUI(main, ino).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Ino"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
