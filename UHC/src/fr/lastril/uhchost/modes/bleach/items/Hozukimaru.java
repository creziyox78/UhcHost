package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Ikkaku;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Hozukimaru extends QuickItem {
    public Hozukimaru(UhcHost main) {
        super(Material.NETHER_STAR);
        super.setName("§dHozukimaru");
        super.setLore("",
                "§7Octroie§c Force 1§7 et§bSpeed 1",
                "§7pendant 5 minutes.",
                "§7(Cooldown - 10 minutes)");
        super.glow(true);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Ikkaku){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownHozukimaru() <= 0){
                        if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        if(player.hasPotionEffect(PotionEffectType.SPEED))
                            player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*5*60, 0, false, false));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5*60, 0, false, false));
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        playerManager.setRoleCooldownHozukimaru(10*60);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHozukimaru()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else{
                player.sendMessage(Messages.not("Ikkaku"));
            }
        });
    }
}
