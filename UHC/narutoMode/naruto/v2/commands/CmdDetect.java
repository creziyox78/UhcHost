package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.component.DetectComponent;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.tasks.DetectTask;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.world.WorldUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CmdDetect implements ModeSubCommand {

    private final UhcHost main;

    public CmdDetect(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "detect";
    }

    @Override
    public List<String> getSubArgs() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (playerManager.hasRole()) {
            if (playerManager.getRole() instanceof Madara) {
                Madara madara = (Madara) playerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {

                    if (madara.getDetectUses() < 2) {
                        if (args.length == 2) {
                            String targetName = args[1];
                            Player target = Bukkit.getPlayer(targetName);
                            if (target != null) {
                                new DetectTask(main, madara, target.getUniqueId()).runTaskTimer(main, 0, 20);
                                madara.useDetect();
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traquez désormais " + target.getName());
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        } else {
                            List<Player> playersAround200Blocs = Bukkit.getOnlinePlayers().stream()
                                    .filter(player1 -> player1 != player)
                                    .filter(player1 -> player1.getGameMode() != GameMode.SPECTATOR)
                                    .filter(player1 -> main.getPlayerManager(player1.getUniqueId()).isAlive())
                                    .filter(player1 -> WorldUtils.getDistanceBetweenTwoLocations(player.getLocation(), player1.getLocation()) < 200).collect(Collectors.toList());


                            if (!playersAround200Blocs.isEmpty()) {
                                TextComponent message = new TextComponent("§cTraquer §7: ");

                                for (Player target : playersAround200Blocs) {
                                    message.addExtra(new DetectComponent(target.getName()).toText());
                                }

                                player.spigot().sendMessage(message);
                            } else {
                                player.sendMessage(Messages.error("Vous n'avez personne à traquer aux alentours de 200 blocs."));
                            }
                        }
                    } else {
                        player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Madara"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
