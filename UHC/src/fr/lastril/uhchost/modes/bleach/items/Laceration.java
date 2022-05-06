package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.arrancars.espada.Grimmjow;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Laceration extends QuickItem {
    public Laceration(UhcHost main) {
        super(Material.INK_SACK, 1, (byte)5);
        super.glow(true);
        super.setName("§cLacération");
        super.setLore("",
                "§7Permet d'effectuer un dash",
                "§7sur 10 blocs. Si un joueur est touché",
                "§7par ce dash, il sera immobilisé pendant",
                "§73 secondes.");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Grimmjow) {
                Grimmjow grimmjow = (Grimmjow) playerManager.getRole();
                if(bleachPlayerManager.canUsePower()) {
                    if(bleachPlayerManager.isInFormeLiberer()){
                        if(playerManager.getRoleCooldownLaceration() <= 0) {
                            new BukkitRunnable() {
                                int time = 20*6;
                                @Override
                                public void run() {
                                    player.setVelocity(player.getLocation().getDirection().multiply(1.2D).setY(0));
                                    Location location = player.getLocation();
                                    location.getWorld().getNearbyEntities(location, 1.0D, 1.0D, 1.0D).forEach(entity -> {
                                        if (entity instanceof Player) {
                                            Player players = (Player)entity;
                                            if(players != player) {
                                                PlayerManager playerManagers = main.getPlayerManager(players.getUniqueId());
                                                if (playerManagers.isAlive()) {
                                                    playerManagers.stun(players.getLocation());
                                                    grimmjow.setStunned(players);
                                                    players.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous avez été immobilisé pendant 3 secondes dû à une lacération.");
                                                    Bukkit.getScheduler().runTaskLater(main, () -> {
                                                        grimmjow.setStunned(null);
                                                        playerManagers.setStunned(false);
                                                        players.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§aVous pouvez à nouveau vous déplacer.");
                                                    }, 20*3);
                                                    cancel();
                                                }
                                            }

                                        }
                                    });
                                    if (time <= 0)
                                        cancel();
                                    time--;
                                }

                            }.runTaskTimer(main, 0, 1);
                            playerManager.setRoleCooldownLaceration(120);
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRugissement()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous devez être en forme libérée pour utiliser ce pouvoir.");
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Grimmjow"));
            }
        });
    }
}
