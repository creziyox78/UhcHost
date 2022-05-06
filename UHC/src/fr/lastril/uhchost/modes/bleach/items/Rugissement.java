package fr.lastril.uhchost.modes.bleach.items;

import com.avaje.ebeaninternal.server.core.Message;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Grimmjow;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Rugissement extends QuickItem {
    public Rugissement(UhcHost main) {
        super(Material.INK_SACK, 1, (byte)6);
        super.glow(true);
        super.setName("§cRugissement");
        super.setLore("",
                "§7Repousse les joueurs de 5 blocs",
                "§7en arrière dans un rayon ",
                "§7de 7 blocs autour de l'utilisateur",
                "(Cooldown - 2 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Grimmjow) {
                if(bleachPlayerManager.canUsePower()) {
                    if(bleachPlayerManager.isInFormeLiberer()){
                        if(playerManager.getRoleCooldownRugissement() <= 0) {
                            ClassUtils.ripulseEntityFromLocation(player.getLocation(), 7, 2, 1);
                            playerManager.setRoleCooldownRugissement(120);
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRugissement()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous devez être en forme libérée pour utiliser ce pouvoir.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Grimmjow"));
            }
        });
    }
}
