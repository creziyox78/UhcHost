package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Hinata;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Neji;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Danzo;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HakkeItem extends QuickItem {

    private final UhcHost main = UhcHost.getInstance();

    private final int distance = 10;
    private final int health = 2;

    public HakkeItem() {
        super(Material.NETHER_STAR);
        super.setName("§bHakke");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Hinata || PlayerManager.getRole() instanceof Neji) {
                    if (!main.getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownHakke() == 0) {
                            playerClick.sendMessage(
                                    Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                            PlayerManager.setRoleCooldownHakke(20 * 60);
                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownHakke(), playerClick.getItemInHand());
                            for (Entity entity : playerClick.getNearbyEntities(distance, distance, distance)) {
                                if (entity instanceof Player) {
                                    Player nearPlayer = (Player) entity;
                                    PlayerManager nearPlayerManager = main.getPlayerManager(nearPlayer.getUniqueId());
                                    if (nearPlayer != playerClick && nearPlayer.getGameMode() != GameMode.SPECTATOR && nearPlayerManager.isAlive()) {
                                        nearPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()
                                                + "§bVous venez d'être frappé par l'Hakke.");
                                        if (nearPlayer.getHealth() - (2D * health) <= 0) {
                                            if (nearPlayerManager.getRole() instanceof Danzo) {
                                                Danzo danzo = (Danzo) nearPlayerManager.getRole();
                                                danzo.useVie(nearPlayerManager, 6);
                                            } else {
                                                nearPlayer.damage(20);
                                            }
                                        } else {
                                            nearPlayer.setHealth(nearPlayer.getHealth() - (2D * health));
                                            nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
                                                    20 * 3, 0, false, false));
                                            nearPlayer.addPotionEffect(
                                                    new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
                                        }
                                    }
                                }
                            }
                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownHakke()));
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

}
