package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Halibel;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Cascada extends QuickItem {
    public Cascada(UhcHost main) {
        super(Material.SPONGE);
        super.setName("§bCascada");
        super.glow(true);
        super.setLore("§7",
                "§7Fait apparaitre un ",
                "§9cube d'eau§7 de 20x20x20 ",
                "§7qui dure 1 minute.",
                "§7(Cooldwon - 3 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.getRole() instanceof Halibel) {
                Halibel halibel = (Halibel) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownCascada() <= 0) {
                        halibel.createWater(player.getLocation());
                        playerManager.setRoleCooldownCascada(60*3);
                        Bukkit.getScheduler().runTaskLater(main, halibel::deleteWater, 20*60);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownCascada()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });

    }
}
