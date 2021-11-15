package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.atlantis.api.utils.FormatTime;
import fr.atlantis.api.utils.Title;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

public class NinjutsuItem extends QuickItem {

    public NinjutsuItem(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§4Ninjutsu Spatio-Temporel");
        super.onClick(onClick -> {
            Player playerClick = onClick.getPlayer();
            PlayerManager PlayerManager = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
            if (onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (PlayerManager.getRole() instanceof Obito) {
                    Obito obito = (Obito) PlayerManager.getRole();
                    if (!UhcHost.getInstance().getNarutoV2Manager().isInSamehada(playerClick.getUniqueId())) {
                        if (PlayerManager.getRoleCooldownNinjutsu() <= 0) {
                            obito.setUseNinjutsu(!obito.isUseNinjutsu());
                            if (obito.isUseNinjutsu()) {
                                for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                    if (PlayerManagers.getPlayer() != null) PlayerManagers.getPlayer().hidePlayer(playerClick);
                                }
                            } else {
                                for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                    if (PlayerManagers.getPlayer() != null) PlayerManagers.getPlayer().showPlayer(playerClick);
                                }
                            }
                            if (!obito.isHasUseNinjutsu()) {
                                obito.setHasUseNinjutsu(true);
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        ActionBar.sendMessage(playerClick, "§eTemps d'invisibilité restant : " + new FormatTime(obito.getTimeInvisibleRemining()));
                                        if (obito.isUseNinjutsu()) {

                                            if (obito.getTimeInvisibleRemining() <= 0) {
                                                PlayerManager.setRoleCooldownNinjutsu(5 * 60);
                                                obito.setTimeInvisibleRemining(60);
                                                for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                                    if (PlayerManagers.getPlayer() != null)
                                                        PlayerManagers.getPlayer().showPlayer(playerClick);
                                                }
                                                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cLe temps est écoulé. Vous n'êtes plus invisible aux yeux des autres PlayerManagers.");
                                                obito.setHasUseNinjutsu(false);
                                                obito.setUseNinjutsu(false);
                                                cancel();
                                            }
                                            obito.setTimeInvisibleRemining(obito.getTimeInvisibleRemining() - 1);
                                        } else {
                                            PlayerManager.setRoleCooldownNinjutsu(5 * 60);
                                            PlayerManager.sendTimer(playerClick, PlayerManager.getRoleCooldownNinjutsu(), NinjutsuItem.super.toItemStack());
                                            obito.setTimeInvisibleRemining(60);
                                            for (PlayerManager PlayerManagers : main.getPlayerManagersAlives()) {
                                                if (PlayerManagers.getPlayer() != null)
                                                    PlayerManagers.getPlayer().showPlayer(playerClick);
                                            }
                                            obito.setHasUseNinjutsu(false);
                                            obito.setUseNinjutsu(false);
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(main, 0, 20);
                            }

                            playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes désormais "
                                    + (obito.isUseNinjutsu() ? "invisible" : "visible") + " aux yeux des autres PlayerManagers !");

                        } else {
                            playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownNinjutsu()));
                        }
                    } else {
                        playerClick
                                .sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }

                }
            }
        });
    }

}
