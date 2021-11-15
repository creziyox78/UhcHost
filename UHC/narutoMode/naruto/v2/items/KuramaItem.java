package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class KuramaItem extends QuickItem {

    private static final int TIME_EFFECTS = 5 * 60 * 20;

    public KuramaItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Kurama");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof KuramaUser) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownKurama() == 0) {
                            KuramaUser kuramaUser = (KuramaUser) PlayerManager.getRole();
                            kuramaUser.setCanUseSmell(true);
                            if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, TIME_EFFECTS, 0, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TIME_EFFECTS, 1, false, false));
                            main.getSoundUtils().playSoundDistance(player.getLocation(), 5, "atlantis.kyuubimode");
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownKurama(20 * 60);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownKurama(), player.getItemInHand());
                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    PlayerManager.getRole().getEffects().entrySet().stream().filter(e -> e.getValue() == When.START).forEach(e -> {
                                        if (PlayerManager.getPlayer() != null) {
                                            if (PlayerManager.getPlayer().hasPotionEffect(e.getKey().getType())) {
                                                PlayerManager.getPlayer().removePotionEffect(e.getKey().getType());
                                            }
                                            PlayerManager.getPlayer().addPotionEffect(e.getKey());
                                        }
                                    });
                                }

                            }.runTaskLater(main, TIME_EFFECTS + 40);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownKurama()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Minato"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface KuramaUser {

        void setCanUseSmell(boolean state);

        boolean canUseSmell();

    }

}
