package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.gui.ZommariEffect;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Zommari;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Effet extends QuickItem {
    public Effet(UhcHost main) {
        super(Material.INK_SACK);
        super.setName("§fEffet");
        super.setLore("",
                "§7Permet de copier un effet",
                "§7du joueur ciblé pendant 5 minutes.",
                "§6",
                "§6Si vous possédiez un effet, il sera remplacé.",
                "",
                "§eSous forme libéré, si l'effet que vous aviez",
                "§eest est le même que celui copié, l'effet passera",
                "§eau niveau supérieur.");
        super.glow(true);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Zommari) {
                Zommari zommari = (Zommari) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    Player target = ClassUtils.getTargetPlayer(player, 10);
                    if(target != null) {
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        if(targetManager.isAlive()) {
                            new ZommariEffect(zommari, targetManager).open(player);
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cLe joueur ciblé est mort.");
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cAucun joueur ciblé.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas Zommari.");
            }
        });
    }
}
