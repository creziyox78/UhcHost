package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Yoruichi;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kido extends QuickItem {
    public Kido(UhcHost main) {
        super(Material.SAPLING, 1, (byte) 1);
        super.setName("§aKido");
        super.setLore("",
                "§7Octroie l'effet§c Force 1",
                "§7pendant 5 minutes à condition",
                "§7que l'utilisateur soit à moins de",
                "§c5 coeurs.",
                "§7(Cooldown - 15 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Yoruichi) {
                Yoruichi yoruichi = (Yoruichi) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownKido() <= 0) {
                        if(player.getHealth() <= 2D*5D) {
                            yoruichi.setInKido(true);
                            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5*60*20, 0, false, false));
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§bVous avez utilisé votre §aKido§b !");
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez être à moins de §c5 coeurs§c pour utiliser votre §aKido§c !");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownKido()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("§aYoruichi"));
            }
        });
    }
}
