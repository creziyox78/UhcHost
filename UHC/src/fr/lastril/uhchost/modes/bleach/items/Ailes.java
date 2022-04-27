package fr.lastril.uhchost.modes.bleach.items;

import com.avaje.ebeaninternal.server.core.Message;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Ulquiorra;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Ailes extends QuickItem {
    public Ailes(UhcHost main) {
        super(Material.FEATHER);
        super.setName("§fAiles");
        super.glow(true);
        super.setLore("",
                "§7Permet de s'envoler pendant",
                "§f5 secondes§7.",
                "§7(Cooldown - 2 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Ulquiorra){
                if(bleachPlayerManager.canUsePower()){
                    if(bleachPlayerManager.isInFormeLiberer()){
                        if(playerManager.getRoleCooldownAiles() <= 0){
                            player.setAllowFlight(true);
                            playerManager.setRoleCooldownAiles(60*2);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous pouvez vous envoler pendant §f5 secondes§7.");
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                player.setFlying(false);
                                player.setAllowFlight(false);
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous ne pouvez plus vous envoler.");
                            }, 20*5);
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownAiles()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas sous forme libéré.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Ulquiorra"));
            }
        });
    }
}
