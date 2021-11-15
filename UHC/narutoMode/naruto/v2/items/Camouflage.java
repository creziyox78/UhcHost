package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.zabuza.Zabuza;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Camouflage extends QuickItem {
    public Camouflage(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§3Camouflage");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Zabuza) {
                    Zabuza zabuza = (Zabuza) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        if (!zabuza.isCamoufled()) {
                            if (PlayerManager.getRoleCooldownCamouflage() <= 0) {
                                zabuza.setCamoufled(true);
                                zabuza.hidePlayer(player);
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§c invisible§e (même avec votre armure) aux yeux de tous maintenant.");
                                return;
                            } else {
                                player.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownCamouflage()));
                            }
                        } else {
                            PlayerManager.setRoleCooldownCamouflage(5 * 60);
                            zabuza.setCamoufled(false);
                            zabuza.showPlayer(player);
                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous êtes§a visible§e aux yeux de tous maintenant.");
                            return;
                        }
                    } else {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    }
                }
            }
        });
    }
}
