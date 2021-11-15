package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Hinata;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Neji;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ByakuganItem extends QuickItem {

    private final UhcHost main = UhcHost.getInstance();

    private final int distance = 30;

    public ByakuganItem() {
        super(Material.NETHER_STAR);
        super.setName("§bByakugan");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Hinata || PlayerManager.getRole() instanceof Neji) {
                    if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownByakugan() == 0) {
                            playerClick.sendMessage(
                                    Messages.NARUTO_PREFIX.getMessage() + "§bPlayerManagers se trouvant à 60 blocs de vous.");
                            PlayerManager.setRoleCooldownByakugan(20 * 60);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownByakugan(), playerClick.getItemInHand());
                            for (Entity entity : playerClick.getNearbyEntities(distance, distance, distance)) {
                                if (entity instanceof Player) {
                                    Player nearPlayer = (Player) entity;
                                    PlayerManager PlayerManagers = main.getPlayerManager(nearPlayer.getUniqueId());
                                    if (PlayerManagers.isAlive() || nearPlayer.getGameMode() != GameMode.SPECTATOR) {
                                        playerClick.sendMessage(getCardinalDirection(playerClick, nearPlayer));
                                    }
                                }
                            }
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownByakugan()));
                        }

                    } else {
                        playerClick.sendMessage(
                                Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                } else {
                    playerClick.sendMessage(Messages.not("Hinata ou Neji"));
                }
            }


        });
    }

    public String getCardinalDirection(Player player, Player entity) {
        Vector playerToEntity = entity.getLocation().clone().subtract(player.getLocation()).toVector();
        Vector playerLooking = player.getLocation().getDirection();
        double x1 = playerToEntity.getX();
        double z1 = playerToEntity.getZ();
        double x2 = playerLooking.getX();
        double z2 = playerLooking.getZ();
        double distance = player.getLocation().distance(entity.getLocation());
        String message = "§3";
        double angle = Math.atan2(x1 * z2 - z1 * x2, x1 * x2 + z1 * z2) * 180 / Math.PI;
        if (angle >= -45 && angle < 45) {
            message += entity.getName() + " : Position - NORD ";
        } else if (angle >= 45 && angle < 135) {
            message += entity.getName() + " : Position - OUEST ";
        } else if (angle >= 135 && angle <= 180 || angle >= -180 && angle < -135) {
            message += entity.getName() + " : Position - SUD ";
        } else if (angle >= -135 && angle < -45) {
            message += entity.getName() + " : Position - EST ";
        }
        if (distance < 0)
            distance *= -1;
        message += "❘ Distance - " + (int) distance + "m";


        return message;
    }

}
