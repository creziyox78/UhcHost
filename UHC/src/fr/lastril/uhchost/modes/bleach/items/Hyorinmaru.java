package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.ToshiroHitsugaya;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitTask;

public class Hyorinmaru extends QuickItem {

    private BukkitTask task;

    public Hyorinmaru(UhcHost main) {
        super(Material.FEATHER);
        super.setName("§fHyorinmaru");
        super.setLore("",
                "§7Permet de s'envoler pendant",
                "§710 secondes. (Cooldown - 7 minutes)");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof ToshiroHitsugaya){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownHyorinmaru() <= 0){
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        task = Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
                            int timer = 10*20;
                            @Override
                            public void run() {
                                if(timer == 0){
                                    player.setAllowFlight(false);
                                    player.setFlying(false);
                                    task.cancel();
                                }
                                ActionBar.sendMessage(player, "§7Hyorinmaru : [" + ChunkLoader.getProgressBar(timer/20, 10, 10, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                timer--;
                            }
                        }, 0, 1);
                        playerManager.setRoleCooldownHyorinmaru(7*60);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHyorinmaru()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
