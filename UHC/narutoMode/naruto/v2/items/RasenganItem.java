package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RasenganItem extends QuickItem {

    private static final int TIME_EFFECTS = 5 * 60 * 20;

    public RasenganItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Rasengan");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof RasenganUser) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownRasengan() == 0) {
                            RasenganUser rasenganUser = (RasenganUser) PlayerManager.getRole();
                            rasenganUser.setAttackBoosted(true);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownRasengan(5 * 60);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownRasengan(), player.getItemInHand());
                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    PlayerManager.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                        if (PlayerManager.getPlayer() != null) {
                                            PlayerManager.getPlayer().addPotionEffect(e.getKey());
                                        }
                                    });
                                }

                            }.runTaskLater(main, TIME_EFFECTS + 40);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownRasengan()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Konohamaru"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface RasenganUser {

        boolean isAttackBoosted();

        void setAttackBoosted(boolean state);

    }

}
