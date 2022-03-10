package fr.lastril.uhchost.modes.lg.commands.trublion;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.solo.Trublion;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSwitch implements ModeSubCommand {

    private final UhcHost main;

    public CmdSwitch(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "switch";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
        if(playerManager.hasRole() && playerManager.isAlive()){
            if(playerManager.getRole() instanceof Trublion){
                Trublion trublion = (Trublion) playerManager.getRole();
                if(!trublion.isSwitched()){
                    String targetName1 = args[1];
                    String targetName2 = args[2];
                    Player target1 = Bukkit.getPlayer(targetName1);
                    Player target2 = Bukkit.getPlayer(targetName2);
                    if(target1 != null && target2 != null){
                        if(wolfPlayerManager.isSarbacaned()){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                            return false;
                        }
                        PlayerManager targetManager1 = main.getPlayerManager(target1.getUniqueId());
                        PlayerManager targetManager2 = main.getPlayerManager(target2.getUniqueId());
                        trublion.applySwitch(playerManager,targetManager1, targetManager2);
                    } else {
                        player.sendMessage(Messages.error("L'un de ces 2 joueurs ne sont pas connectés !"));
                    }

                } else {
                    player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
                }
            } else {
                player.sendMessage(Messages.not("Trublion"));
            }
        }
        return false;
    }
}
