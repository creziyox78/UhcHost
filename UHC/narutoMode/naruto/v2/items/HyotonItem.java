package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.zabuza.Haku;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class HyotonItem extends QuickItem {


    public HyotonItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§bHyôton");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Haku) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownHyoton() <= 0) {
                            Haku haku = (Haku) PlayerManager.getRole();
                            if (haku.getPhase() == 1) {
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§bVous venez de créer une sphère, vous pouvez maintenant vous téléporter (avec le même item) sur le bloque que vous regarder.");
                                haku.setLoc(player.getLocation());
                                haku.addPhase();
                                for (Location location : haku.sphere(haku.getLoc(), 18, true)) {
                                    if (location.getBlock().getType() == Material.AIR) {
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                for (Location location : haku.sphere(haku.getLoc(), 17, true)) {
                                    if (location.getBlock().getType() == Material.AIR) {
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                for (Location location : haku.sphere(haku.getLoc(), 16, true)) {
                                    if (location.getBlock().getType() == Material.AIR) {
                                        location.getBlock().setType(Material.PACKED_ICE);
                                        haku.getIceUnbreakabke().add(location);
                                    }
                                }
                                Bukkit.getScheduler().runTaskLater(main, () -> {
                                    for (Location location : haku.sphere(haku.getLoc(), 18, true)) {
                                        if (location.getBlock().getType() == Material.PACKED_ICE) {
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    for (Location location : haku.sphere(haku.getLoc(), 17, true)) {
                                        if (location.getBlock().getType() == Material.PACKED_ICE) {
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    for (Location location : haku.sphere(haku.getLoc(), 16, true)) {
                                        if (location.getBlock().getType() == Material.PACKED_ICE) {
                                            location.getBlock().setType(Material.AIR);
                                            haku.getIceUnbreakabke().remove(location);
                                            haku.resetPhase();
                                        }
                                    }
                                    PlayerManager.setRoleCooldownHyoton(15 * 60);
                                }, 20 * 60 * 2);


                            }
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownHyoton()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }
        });
    }
}
