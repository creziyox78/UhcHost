package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RasenganItem extends QuickItem {

    private static final int TIME_EFFECTS = 5 * 60 * 20;
    private NarutoV2Manager narutoV2Manager;

    public RasenganItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Rasengan");
        super.onClick(onClick -> {
            narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
            Player player = onClick.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if (joueur.hasRole()) {
                if (joueur.getRole() instanceof RasenganUser) {
                    if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                        if (joueur.getRoleCooldownRasengan() == 0) {
                            RasenganUser rasenganUser = (RasenganUser) joueur.getRole();
                            rasenganUser.setAttackBoosted(true);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            joueur.setRoleCooldownRasengan(5 * 60);
                            joueur.sendTimer(player, joueur.getRoleCooldownRasengan(), player.getItemInHand());
                            if (joueur.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                narutoRole.usePower(joueur);
                                narutoRole.usePowerSpecific(joueur, super.getName());
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    joueur.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                        if (joueur.getPlayer() != null) {
                                            joueur.getPlayer().addPotionEffect(e.getKey());
                                        }
                                    });
                                }

                            }.runTaskLater(main, TIME_EFFECTS + 40);
                        } else {
                            player.sendMessage(Messages.cooldown(joueur.getRoleCooldownRasengan()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Konohamaru"));
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            }
        });
    }

    public interface RasenganUser {

        void setAttackBoosted(boolean state);

        boolean isAttackBoosted();

    }

}
