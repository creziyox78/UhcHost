package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Detective;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.isAlive()){
            if(playerManager.getRole() instanceof Detective){
                Detective detective = (Detective) playerManager.getRole();
                if(!detective.isDetected()){
                    if(args.length == 3){
                        String targetName1 = args[1];
                        String targetName2 = args[2];
                        Player target1 = Bukkit.getPlayer(targetName1);
                        Player target2 = Bukkit.getPlayer(targetName2);
                        if(target1 != null && target2 != null){
                            PlayerManager targetManager1 = main.getPlayerManager(target1.getUniqueId());
                            PlayerManager targetManager2 = main.getPlayerManager(target2.getUniqueId());
                            boolean sameCamp = targetManager1.getCamps() == targetManager2.getCamps();
                            detective.setDetected(true);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + (sameCamp ? "§a" + targetName1 + " et " +targetName2 + " sont du même camp." : "§c" + targetName1 + " et " +targetName2 + " ne sont pas du même camp."));
                            UhcHost.debug("Detective compared " + targetManager1.getPlayerName() + " and " + targetManager2.getPlayerName() + " , result : " + sameCamp);
                        } else {
                            player.sendMessage(Messages.error("L'un de ces 2 joueurs ne sont pas connectés !"));
                        }
                    }
                } else {
                    player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Detective"));
            }
        }
        return false;
    }
}
