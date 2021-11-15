package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Nagato;
import fr.maygo.uhc.obj.PlayerManager;
import fr.maygo.uhc.task.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Oiseautem extends QuickItem {

    public Oiseautem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cOiseau");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Nagato) {
                    Nagato nagato = (Nagato) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownOiseau() <= 0) {
                            player.setAllowFlight(true);
                            player.setVelocity(player.getVelocity().add(new Vector(0, 2, 0)));
                            player.setFlying(true);
                            player.setFlySpeed((float) 0.1);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous pouvez désormais voler !");
                            PlayerManager.setRoleCooldownOiseau(20 * 60);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownOiseau(), player.getItemInHand());
                            if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                narutoRole.usePower(PlayerManager);
                            }

                            new BukkitRunnable() {

                                int timer = Nagato.getFlyTime() * 20;

                                @Override
                                public void run() {
                                    ActionBar.sendMessage(player, "§7Oiseau : [" + ChunkLoader.getProgressBar(timer, Nagato.getFlyTime() * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                    if (timer == 0) {
                                        player.setFlying(false);
                                        player.setAllowFlight(false);
                                        main.getNarutoV2Manager().getNofallPlayerManager().add(PlayerManager);
                                        cancel();
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(main, 0, 1);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    main.getNarutoV2Manager().getNofallPlayerManager().remove(PlayerManager);
                                }
                            }.runTaskLater(main, 20 * 20);

                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownOiseau()));
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Nagato"));
                    return;
                }
            } else {
                player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
                return;
            }
        });
    }
}
