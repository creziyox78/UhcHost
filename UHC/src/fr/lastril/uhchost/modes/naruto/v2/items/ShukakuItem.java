package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShukakuItem extends QuickItem {

    private NarutoV2Manager narutoV2Manager;

    public ShukakuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cShukaku");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof Gaara) {
                    Gaara gaara = (Gaara) joueur.getRole();
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownShukaku() <= 0) {
                            gaara.setInShukaku(true);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous êtes sous Shukaku");
                            joueur.setRoleCooldownShukaku(20*60);
                            new BukkitRunnable(){

                                @Override
                                public void run() {
                                    gaara.setInShukaku(false);
                                }
                            }.runTaskLater(main, 20*60*5);
                            gaara.usePower(joueur);
                            gaara.usePowerSpecific(joueur, super.getName());
                        }else{
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownShukaku()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Gaara"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }
}
