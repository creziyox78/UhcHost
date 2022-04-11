package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.InoueOrihime;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Mur extends QuickItem {
    public Mur(UhcHost main) {
        super(Material.NETHER_BRICK_ITEM);
        super.glow(true);
        super.setLore("",
                "§7Créer un§e mur§7 de verre",
                "§7(11x4) centré sur le bloc cliqué",
                "§7L'utilisateur est le seul à pouvoir",
                "§7le détruire.",
                "§7(Cooldown - 3 minutes)");
        super.setName("§eMur");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            Block block = onClick.getTargetBlock();
            if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime) {
                InoueOrihime inoueOrihime = (InoueOrihime) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownMur() <= 0) {
                        if(block != null && block.getType() != Material.AIR) {
                            inoueOrihime.createWall(player, block.getLocation());
                            Bukkit.getScheduler().runTaskLater(main, inoueOrihime::deleteWall, 20*60*3L);
                            playerManager.setRoleCooldownMur(180);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez créé un mur de verre.");
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez cliquer sur un bloc.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownMur()));
                    }
                } else{
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Inoue Orihime"));
            }
        });
    }
}
