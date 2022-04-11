package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.IchigoKurosaki;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HollowMask extends QuickItem {
    public HollowMask(UhcHost main) {
        super(Material.GOLD_HELMET);
        super.setName("§eHollow Mask");
        super.glow(true);
        super.setLore("",
                "§7Octroie l'effet§b Speed 1§7 ainsi",
                "§7que§e 5%§c de Force§7",
                "§7pendant 2 minutes.",
                "§7(Cooldown - 8 minutes)");
        super.onClick(onCLick -> {
            Player player = onCLick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof IchigoKurosaki){
                IchigoKurosaki ichigoKurosaki = (IchigoKurosaki) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownHollowMask() <= 0){
                        bleachPlayerManager.setStrengthPourcentage(bleachPlayerManager.getStrengthPourcentage() + 5);
                        if(player.hasPotionEffect(PotionEffectType.SPEED))
                            player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*2, 0, false, false));
                        ichigoKurosaki.setInMask(true);
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            ichigoKurosaki.setInMask(false);
                            bleachPlayerManager.setStrengthPourcentage(bleachPlayerManager.getStrengthPourcentage() - 5);
                        }, 20*60*2);
                        playerManager.setRoleCooldownHollowMask(60*8);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHollowMask()));
                    }

                }
            }
        });
    }
}
