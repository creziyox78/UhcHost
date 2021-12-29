package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.GenjutsuGUI;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class GenjutsuItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public GenjutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cGenjutsu");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof GenjutsuUser) {
                    GenjutsuUser genjutsuUser = (GenjutsuUser) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
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

    public interface GenjutsuUser{

        void useTsukuyomi();
        int getTsukuyomiUses();

        void useIzanami();
        boolean hasUsedIzanami();

        boolean hasKilledUchiwa();
        boolean isCompleteIzanami();
        void setCompleteIzanami(boolean completeIzanami);

        IzanamiGoal getIzanamiGoal();
        void setIzanamiGoal(IzanamiGoal izanamiGoal);

    }

}
