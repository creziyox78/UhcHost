package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.orochimaru.Tayuya;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

public class FluteDemoniaque extends QuickItem {

    private final int nbGolemSpawn = 3;
    private final int nbEndermiteSpawn = 5;

    public FluteDemoniaque(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dFlûte Démoniaque");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Tayuya) {
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownFluteDemoniaque() <= 0) {
                            Player nearestPlayer = null;
                            double nearestDistance = Integer.MAX_VALUE;
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getGameMode() != GameMode.SPECTATOR && players != player) {
                                    if (player.getLocation().distance(players.getLocation()) < nearestDistance) {
                                        nearestPlayer = players;
                                        nearestDistance = player.getLocation().distance(players.getLocation());
                                    }
                                }
                            }
                            for (int i = 0; i < nbGolemSpawn; i++) {
                                IronGolem golem = player.getLocation().getWorld().spawn(player.getLocation(), IronGolem.class);
                                golem.setCustomName("§dServant de Tayuya");
                                golem.setPlayerCreated(false);
                                if (nearestPlayer != null)
                                    golem.setTarget(nearestPlayer);
                            }
                            for (int i = 0; i < nbEndermiteSpawn; i++) {
                                Endermite endermite = player.getLocation().getWorld().spawn(player.getLocation(), Endermite.class);
                                endermite.setCustomName("§dServant de Tayuya");
                                if (nearestPlayer != null)
                                    endermite.setTarget(nearestPlayer);

                            }
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownFluteDemoniaque(60 * 30);
                            PlayerManager.sendTimer(player, PlayerManager.getRoleCooldownFluteDemoniaque(), player.getItemInHand());
                        } else {
                            player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownFluteDemoniaque()));
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    player.sendMessage(Messages.not("Tayuya"));
                    return;
                }
            }
        });
    }

}
