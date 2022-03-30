package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.IchigoKurosaki;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdMugetsu implements ModeSubCommand {

    private final UhcHost main;

    public CmdMugetsu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "mugetsu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.hasRole() && playerManager.isAlive() && playerManager.getRole() instanceof IchigoKurosaki) {
            IchigoKurosaki ik = (IchigoKurosaki) playerManager.getRole();
            if(ik.isWhite()) {
                if(!ik.hasUsedMugetsu()) {
                    if(bleachPlayerManager.canUsePower()){
                        ik.setUsedMugetsu(true);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§6Vous avez utilisé Mugetsu !");
                        for(Entity entity : player.getNearbyEntities(50, 50, 50)) {
                            if(entity instanceof Player) {
                                Player p = (Player) entity;
                                if (p.getUniqueId() != player.getUniqueId()) {
                                    p.sendMessage("§6White§7 »§6 Ceci est la fin de tout...");
                                    p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1, 1);
                                }
                            }
                        }
                        Bukkit.getScheduler().runTaskLater(main, () -> {
                            Location loc = player.getLocation();
                            WorldUtils.createBeautyExplosion(loc, 50);
                            for(Entity entity : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
                                if(entity instanceof Player) {
                                    Player p = (Player) entity;
                                    p.playSound(p.getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
                                    if (p.getUniqueId() != player.getUniqueId()) {
                                        if(p.getLocation().distance(loc) <= 50) {
                                            if(p.getLocation().distance(loc) < 10) {
                                                p.damage(100);
                                            }
                                            else if(p.getLocation().distance(loc) < 30) {
                                                p.damage(2D*7D);
                                            }
                                            else if(p.getLocation().distance(loc) <= 50) {
                                                p.damage(3D*5D);
                                            }
                                        }
                                    }
                                }
                            }
                        }, 20*10);
                        bleachPlayerManager.setBloquedPower(true);
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.ALREADY_USED_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous n'êtes pas sous forme §f\"White\"§c !");
            }
        }
        return false;
    }
}
