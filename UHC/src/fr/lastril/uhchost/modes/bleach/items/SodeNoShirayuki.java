package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Rukia;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SodeNoShirayuki extends QuickItem {
    public SodeNoShirayuki(UhcHost main) {
        super(Material.INK_SACK, (byte)15);
        super.setName("§fSode No Shirayuki");
        super.glow(true);
        super.setLore("",
                "§7Fais tomber§f de la neige (5x5)",
                "§7pendant 30 secondes.",
                "§7Les joueurs pris dedans reçoivent",
                "§8Weakness 1 et§7 Slowness 1.",
                "§7(Cooldown - 7 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Rukia){
                Rukia rukia = (Rukia) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownSodeNoShirayuki() <= 0){
                        if(rukia.isInSode()){
                            if(!rukia.isUsedSuperMode()){
                                playerManager.setRoleCooldownSodeNoShirayuki(7 * 60);
                                rukia.setUsedSuperMode(true);
                                rukia.setDuration(30);
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez utilisé §fSode No Shirayuki§7 à sa pleine puissance !");
                            } else {
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez déjà utilisé §fSode No Shirayuki§7 à son maximum !");
                            }
                        } else {
                            rukia.setInSode(true);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous avez utilisé §fSode No Shirayuki§7 !");
                            if(rukia.isUsedSuperMode()){
                                playerManager.setRoleCooldownSodeNoShirayuki(7 * 60);
                            }
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownSodeNoShirayuki()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
