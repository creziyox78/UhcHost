package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Ulquiorra;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.inventory.crafter.TaskUpdate;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;

public class LanzaDelRelampago extends QuickItem {
    public LanzaDelRelampago(UhcHost main) {
        super(Material.SNOW_BALL, 2);
        super.setName("§fLanza Del Relampago");
        super.glow(true);
        super.setLore("",
                "§7Envoie une boule de neige",
                "§7dans les 5 secondes suivantes",
                "§7Créer une explosion de 20x20 blocs",
                "§7à l'impact. Les joueurs touchés",
                "§7reçoivent 5 coeurs de dégâts.",
                "§7(Cooldown - 6 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            onClick.cancelOriginalUse(true);
            if(playerManager.hasRole() && playerManager.getRole() instanceof Ulquiorra) {
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownLanzaDelRelampago() <= 0) {
                        playerManager.setRoleCooldownLanzaDelRelampago(60*6);
                        new BukkitRunnable() {
                            int ticks = 20;
                            int seconds = 0;
                            @Override
                            public void run() {
                                ticks--;
                                if(ticks == 0) {
                                    ticks = 20;
                                    seconds++;
                                    ActionBar.sendMessage(player, "§8» §fLanza Del Relampago :§7" + ChunkLoader.getProgressBar(seconds, 5, 50, '|', ChatColor.GRAY, ChatColor.WHITE) + "§9 «");
                                }
                                if(seconds == 5) {
                                    Snowball snowball = player.launchProjectile(Snowball.class);
                                    snowball.setCustomName("§fLanza Del Relampago");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(main, 0, 1);
                    }
                }
            }
        });
    }
}
