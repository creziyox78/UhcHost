package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.kakashi.PakkunGUI;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Kakashi;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PakkunItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public PakkunItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Pakkun");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Kakashi) {
                    Kakashi kakashi = (Kakashi) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if(joueur.getRoleCooldownPakkun() == 0){
                            new PakkunGUI(main).open(player);
                        }else{
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownPakkun()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Kakashi"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
