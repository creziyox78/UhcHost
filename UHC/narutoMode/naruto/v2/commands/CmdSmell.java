package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.naruto.v2.items.KuramaItem;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Itachi;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Sai;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Karin;
import fr.maygo.uhc.modes.naruto.v2.roles.taka.Sasuke;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdSmell implements ModeSubCommand {

    private static final int DISTANCE_SAI_SASUKE = 30;
    private final UhcHost main;
    private final List<String> whitelistedSmell;

    public CmdSmell(UhcHost main) {
        this.main = main;
        this.whitelistedSmell = Arrays.asList(new Karin().getRoleName(), new Itachi().getRoleName(), new Obito().getRoleName());
    }


    @Override
    public String getSubCommandName() {
        return "smell";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof KuramaItem.KuramaUser) {
                KuramaItem.KuramaUser kuramaUser = (KuramaItem.KuramaUser) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (kuramaUser.canUseSmell()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                if (targetPlayerManager.hasRole() && targetPlayerManager.isAlive()) {
                                    if (player.getWorld() == target.getWorld() && player.getLocation().distance(target.getLocation()) <= 20) {
                                        kuramaUser.setCanUseSmell(false);
                                        if (targetPlayerManager.getRole() instanceof Sai) {
                                            for (Entity entity : target.getNearbyEntities(DISTANCE_SAI_SASUKE, DISTANCE_SAI_SASUKE, DISTANCE_SAI_SASUKE)) {
                                                if (entity instanceof Player) {
                                                    Player nearby = (Player) entity;
                                                    PlayerManager nearbyPlayerManager = main.getPlayerManager(nearby.getUniqueId());
                                                    if (nearbyPlayerManager.hasRole()) {
                                                        if (nearbyPlayerManager.getRole() instanceof Sasuke) {
                                                            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe PlayerManager possède des intentions meurtrières.");
                                                            return false;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        if (targetPlayerManager.getCamps() != Camps.SHINOBI) {
                                            if (!this.whitelistedSmell.contains(targetPlayerManager.getRole().getRoleName())) {
                                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cCe PlayerManager possède des intentions meurtrières.");
                                                return true;
                                            }
                                        }
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aCe PlayerManager ne possède aucunes intentions meurtrières.");
                                        return true;
                                    } else {
                                        player.sendMessage(Messages.error("Vous n'êtes pas à 20 blocs de ce PlayerManager."));
                                        return false;
                                    }

                                } else {
                                    player.sendMessage(Messages.NOT_INGAME.getMessage());
                                }
                            } else {
                                player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            }
                        }
                    } else {
                        player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre /smell maintenant !"));
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Naruto ou Minato"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
