package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Konan;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.world.tasks.ChunkLoader;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CmdBakuhatsu implements ModeSubCommand {

    private final UhcHost main;

    public CmdBakuhatsu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "bakuhatsu";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (playerManager.hasRole() && playerManager.isAlive()) {
            if (playerManager.getRole() instanceof Konan) {
                Konan konan = (Konan) playerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (playerManager.getRoleCooldownBakuhatsu() <= 0) {
                        playerManager.setRoleCooldownBakuhatsu(20 * 60);
                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());

                        new BukkitRunnable() {

                            int timer = Konan.getExplosionsTime() * 20;

                            @Override
                            public void run() {
                                if (!playerManager.isAlive() || player.getGameMode() == GameMode.SPECTATOR) {
                                    cancel();
                                }
                                ActionBar.sendMessage(player, "§7Bakuhatsu : [" + ChunkLoader.getProgressBar(timer, Konan.getExplosionsTime() * 20, 100, '|', ChatColor.GREEN, ChatColor.WHITE) + "§7]");

                                if (timer % 20 == 0) {
                                    List<Location> blockLocations = CmdBakuhatsu.this.getCircle(player.getLocation(), 10);
                                    for (Location blockLocation : blockLocations) {
                                        blockLocation.getWorld().createExplosion(blockLocation, 1.0f);
                                    }
                                }

                                if (timer == 0) {
                                    cancel();
                                }
                                timer--;
                            }
                        }.runTaskTimer(main, 0, 1);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownBakuhatsu()));
                        return false;
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Konan"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }

    public List<Location> getCircle(Location center, double radius) {
        List<Location> locs = new ArrayList<>();
        int points = 10;

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            Location point = center.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
            locs.add(point);
        }
        return locs;
    }
}
