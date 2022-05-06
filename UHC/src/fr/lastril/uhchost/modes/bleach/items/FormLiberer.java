package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FormLiberer extends QuickItem {
    public FormLiberer(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§fForme libérée");
        super.setLore("",
                "§cRéservé aux Arrancars",
                "§fDébloque la forme libérée d'un§c Arrancar",
                "§7(Cooldown - 3 minutes par détransformation)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.getRole() instanceof ArrancarRole) {
                ArrancarRole arrancarRole = (ArrancarRole) playerManager.getRole();
                if(bleachPlayerManager.getNbQuartzMined() == arrancarRole.getNbQuartz()) {
                    if(bleachPlayerManager.canUsePower()) {
                        if(playerManager.getRoleCooldownFormeLiberer() <= 0) {
                            if(bleachPlayerManager.isInFormeLiberer()) {
                                player.sendMessage("§cVous venez de désactiver votre forme libérée !");
                                arrancarRole.onUnTransformationFirst();
                                bleachPlayerManager.setInFormeLiberer(false);
                            } else {
                                bleachPlayerManager.setInFormeLiberer(true);
                                player.sendMessage("§aVous venez d'activer votre forme libérée !");
                            }
                            playerManager.setRoleCooldownFormeLiberer(3*60);
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez attendre encore §f" + new FormatTime(playerManager.getRoleCooldownFormeLiberer()).toFormatString() + " §cavant de pouvoir vous " + (bleachPlayerManager.isInFormeLiberer() ? "dé-transformé " : "transformé") + " !");
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous ne pouvez pas modifier votre forme pour le moment !");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'avez pas assez de quartz pour vous " + (bleachPlayerManager.isInFormeLiberer() ? "dé-transformé " : "transformé") + " !");
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas un §cArrancar§c !");
            }



        });
    }
}
