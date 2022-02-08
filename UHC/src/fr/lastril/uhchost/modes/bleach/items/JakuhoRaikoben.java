package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.SoiFon;
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
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class JakuhoRaikoben extends QuickItem {


    private BukkitTask task;

    public JakuhoRaikoben(UhcHost main) {
        super(Material.EYE_OF_ENDER, 1);
        super.setName("§6Jakuho Raikoben");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Permet de faire un tir de wither",
                "§7qui inflige§c 5 coeurs de dégâts§7 à",
                "§7tous les joueurs touchés par l'explosion.",
                "§f(Usage unique)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRole() instanceof SoiFon){
                        player.setItemInHand(null);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Chargement du tir...");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1, false, false));
                        onClick.cancelOriginalUse(true);
                        task = Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
                            int timer = 5*20;
                            @Override
                            public void run() {
                                if(timer == 0){
                                    WitherSkull ws = player.launchProjectile(WitherSkull.class);
                                    ws.setVelocity(player.getVelocity().multiply(2));
                                    ws.setIsIncendiary(true);
                                    ws.setYield(player.getLocation().getYaw());
                                    ws.setCustomName("§6Jakuho Raikoben");
                                    ws.setCustomNameVisible(true);
                                    ws.setCharged(true);
                                    task.cancel();
                                }
                                ActionBar.sendMessage(player, "§7Tir : [" + ChunkLoader.getProgressBar(timer, 25, 25, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                timer--;
                            }
                        }, 0, 1);
                    } else {
                        player.sendMessage(Messages.not("Soi Fon"));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }

            }
        });
    }
}
