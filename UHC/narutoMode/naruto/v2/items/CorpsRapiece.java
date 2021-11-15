package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Kakuzu;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CorpsRapiece extends QuickItem {

    private final int distance = 20;
    private final int health = 2;

    public CorpsRapiece(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§cCorps Rapiécé");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Kakuzu) {
                    Kakuzu kakuzu = (Kakuzu) PlayerManager.getRole();
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownCorpsRapiece() <= 0) {
                            PlayerManager.setRoleCooldownCorpsRapiece(60 * 10);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownCorpsRapiece(), playerClick.getItemInHand());
                            kakuzu.usePower(PlayerManager);
                            for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                if (PlayerManagers.getPlayer().getLocation().distance(playerClick.getLocation()) <= distance) {
                                    if (PlayerManagers.getPlayer() != null) {
                                        if (PlayerManagers.getCamps() != PlayerManager.getCamps()) {
                                            PlayerManagers.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cLe corps rapiécé de Kakuzu vous a immobilisé.");
                                            PlayerManagers.stun(PlayerManagers.getPlayer().getLocation());
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    PlayerManagers.setStunned(false);
                                                }
                                            }.runTaskLater(main, 20 * 5);
                                        }
                                    }
                                }
                            }
                        } else {
                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.cooldown(PlayerManager.getRoleCooldownCorpsRapiece()));
                        }
                    } else {
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                        return;
                    }
                } else {
                    playerClick.sendMessage(Messages.not("Kakuzu"));
                    return;
                }
            }
        });
    }

}
