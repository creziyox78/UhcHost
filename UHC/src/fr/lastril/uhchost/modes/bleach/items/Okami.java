package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Stark;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.pathfinder.okami.OkamiInvoker;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Okami extends QuickItem {
    public Okami(UhcHost main) {
        super(Material.INK_SACK, 1, (byte)12);
        super.glow(true);
        super.setName("§bOkami");
        super.setLore("",
                "§7Invoque§f 2 loups§7 possèdants§b Vitesse 2",
                "§7qui explosent à proximité du",
                "§9Shinigami §7le plus proche.",
                "§7(Cooldown - 3 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Stark){
                Stark stark = (Stark) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownOkami() <= 0){
                        if(stark.isLilynetteDead()){
                            for(Entity entity : player.getNearbyEntities(60, 60, 60)){
                                if(entity instanceof Player){
                                    Player target = (Player) entity;
                                    PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                                    if(targetManager.getRole() instanceof ShinigamiRole){
                                        OkamiInvoker.invokeOkami(player.getLocation(), target.getUniqueId());
                                        OkamiInvoker.invokeOkami(player.getLocation(), target.getUniqueId());
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Lilynette est morte, vous ne pouvez plus invoquer de loup.");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownOkami()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Stark"));
            }
        });
    }
}
