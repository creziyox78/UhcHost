package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.items.ReviveItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.KillerBee;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class CmdDeath implements ModeSubCommand {

    private final UhcHost main;

    public CmdDeath(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "death";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (playerManager.hasRole()) {
            if (playerManager.getRole() instanceof KillerBee) {
                KillerBee killerBee = (KillerBee) playerManager.getRole();
                if (killerBee.isUseFakeDeath()) {
                    player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                    return false;
                } else {
                    killerBee.setFakeDeath(true);
                    killerBee.setUseFakeDeath(true);
                    for (PlayerManager PlayerManagers : main.getPlayerManagerAlives()) {
                        if (PlayerManagers.getPlayer() != null) PlayerManagers.getPlayer().hidePlayer(player);
                    }
                    //main.getInvisibleTeam().addEntry(player.getName());
                    main.getInventoryUtils().giveItemSafely(player, new ReviveItem(main).toItemStack());
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
                    }
                    if (playerManager.hasRole()) {
                        Bukkit.broadcastMessage("§3§m----------------------------------");
                        Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était " + playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + "§7.");
                        Bukkit.broadcastMessage("§3§m----------------------------------");
                    }
                    new BukkitRunnable() {
                        int timer = 5 * 60;

                        @Override
                        public void run() {
                            ActionBar.sendMessage(player, "§9Fausse mort: " + new FormatTime(timer));
                            if (timer <= 0) {
                                for (Player players : Bukkit.getOnlinePlayers()) {
                                    players.showPlayer(player);
                                }
                                killerBee.setFakeDeath(false);
                                //main.getInvisibleTeam().removeEntry(player.getName());
                                cancel();
                            }
                            timer--;
                        }
                    }.runTaskTimer(main, 20, 20);
                }
            }
        }
        return false;
    }
}
