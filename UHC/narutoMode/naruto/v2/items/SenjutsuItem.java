package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Jiraya;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Naruto;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class SenjutsuItem extends QuickItem {

    private static final int TIME_EFFECTS = 5 * 60 * 20;

    public SenjutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Senjutsu");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof SenjutsuUser) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownSenjutsu() == 0) {

                            if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
                                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, TIME_EFFECTS, 1, false, false));
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, TIME_EFFECTS, 0, false, false));
                            if (PlayerManager.getRole() instanceof Jiraya) {
                                if (PlayerManager.hasRole()) {
                                    if (PlayerManager.getRole() instanceof Jiraya) {
                                        for (PlayerManager targetPlayerManager : UhcHost.getInstance().getNarutoManager().getPlayerManagersWithRole(Naruto.class)) {
                                            if (targetPlayerManager.getPlayer() != null) {
                                                targetPlayerManager.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + PlayerManager.getRoleName()
                                                        + " vient d'utiliser son pouvoir, il se situe en x: "
                                                        + player.getLocation().getBlockX() + ", y: "
                                                        + player.getLocation().getBlockY() + ", z: "
                                                        + player.getLocation().getBlockZ());
                                            }
                                        }
                                    }
                                }
                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownSenjutsu(30 * 60);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownSenjutsu(), player.getItemInHand());
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
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownSenjutsu()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Naruto ou Jiraya"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }

    public interface SenjutsuUser {
    }
}
