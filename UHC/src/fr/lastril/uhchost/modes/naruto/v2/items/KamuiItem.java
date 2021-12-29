package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.kamui.KamuiGUI;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class KamuiItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;
    public KamuiItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Kamui");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof KamuiUser) {
                    KamuiUser kamuiUser = (KamuiUser) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        new KamuiGUI(main, kamuiUser, player).open(player);
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                }else{
                    player.sendMessage(Messages.not("Kakashi ou Obito"));
                    return;
                }
            }else{
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }


    public interface KamuiUser{

        public int getArimasuCooldown();
        public int getSonohokaCooldown();

        public Map<UUID, Location> getInitialsLocation();

        double getSonohokaDistance();
    }
}
