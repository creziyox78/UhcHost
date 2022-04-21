package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.Sado;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Muerte extends QuickItem {

    private int time = 4;

    public Muerte(UhcHost main) {
        super(Material.QUARTZ);
        super.setName("§6La Muerte");
        super.glow(true);
        super.setLore("§7Effectue un dash vers l'avant",
                "§7sur 4 blocs. Les joueurs touchés auront",
                "§7Slowness 1 pendant 5 secondes.",
                "§7(Cooldown - 1 minute 30 secondes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Sado){
                Sado sado = (Sado) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()){
                    if(playerManager.getRoleCooldownMuerte() <= 0){
                        if(sado.getForm() == Sado.ARMS_FORM.ARM_DIABLE){
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.setVelocity(player.getLocation().getDirection().multiply(1.2D).setY(0));
                                    Location location = player.getLocation();
                                    location.getWorld().getNearbyEntities(location, 2.0D, 2.0D, 2.0D).forEach(entity -> {
                                        if (entity instanceof Player) {
                                            Player players = (Player)entity;
                                            PlayerManager playerManagers = main.getPlayerManager(players.getUniqueId());
                                            if (playerManagers.isAlive()) {
                                                if(players.hasPotionEffect(PotionEffectType.SLOW))
                                                    players.removePotionEffect(PotionEffectType.SLOW);
                                                players.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5 * 20, 0, false, false));
                                            }
                                        }
                                    });
                                    if (time <= 0)
                                        cancel();
                                    time--;
                                }

                            }.runTaskTimer(main, 0, 1);
                            playerManager.setRoleCooldownBrazo(90);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
                        } else {
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez être en forme \"§4Bras du Diable\" pour utiliser ce pouvoir !");
                        }
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownMuerte()));
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        });
    }
}
