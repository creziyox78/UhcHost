package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Karin;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class KarinItem extends QuickItem {

    private final int distance = 100;

    public KarinItem(UhcHost main) {
        super(Material.BOOK);
        super.setName("§6Karin");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
            if (PlayerManager.hasRole()) {
                if (PlayerManager.getRole() instanceof Karin) {
                    Karin karin = (Karin) PlayerManager.getRole();
                    if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Liste des PlayerManagers se trouvant dans un rayon de 100 blocs: ");
                        for (Entity target : player.getNearbyEntities(distance, distance, distance)) {
                            if (target instanceof Player) {
                                Player players = (Player) target;
                                if (players.getGameMode() != GameMode.SPECTATOR && main.getPlayerManager(target.getUniqueId()).isAlive()) {
                                    player.sendMessage("§6- " + players.getName());
                                }
                            }
                        }
                        karin.usePower(PlayerManager);
                    }
                } else {
                    player.sendMessage(Messages.not("Karin"));
                }
            } else {
                player.sendMessage(Messages.NOTHAVE_ROLE.getMessage());
            }
        });
    }

}
