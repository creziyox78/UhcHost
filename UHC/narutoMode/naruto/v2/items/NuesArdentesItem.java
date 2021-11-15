package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
import fr.atlantis.api.utils.WorldUtils;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Asuma;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NuesArdentesItem extends QuickItem {

    public NuesArdentesItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§6Nuées Ardentes");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Asuma) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownNueesArdentes() == 0) {
                            Location explosionLocation = player.getLocation().clone().add(0, 1, 0).add(player.getLocation().getDirection().clone().multiply(6));

                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownNueesArdentes(5 * 60);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownNueesArdentes(), player.getItemInHand());
                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }

                            new BukkitRunnable() {

                                int timer = 3 * 20;

                                @Override
                                public void run() {
                                    WorldUtils.spawnParticle(explosionLocation, EnumParticle.CLOUD, 0.5f, 1f, 0.5f, 0.1f, 100);
                                    ActionBar.sendMessage(player, "§7Explosion : [" + ChunkLoader.getProgressBar(timer, 5 * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                    if (timer == 0) {
                                        explosionLocation.getWorld().createExplosion(explosionLocation, 3.0f);

                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 1);
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownNueesArdentes()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Asuma"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
