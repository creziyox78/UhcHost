package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.gui.ShotGui;
import fr.lastril.uhchost.modes.lg.roles.village.Chasseur;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdShot implements ModeSubCommand {

    private final UhcHost main;

    public CmdShot(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "tir";
    }

    @Override
    public List<String> getSubArgs() {
        return main.getPlayerManagerAlives().stream().filter(playerManager -> playerManager.getPlayer() != null).map(PlayerManager::getPlayerName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole()){
            if(playerManager.getRole() instanceof Chasseur){
                Chasseur chasseur = (Chasseur) playerManager.getRole();
                if(!chasseur.isShot()){
                    if(args.length == 1){
                        new ShotGui(chasseur).open(player);
                    } else if(args.length == 2){
                        String targetName = args[1];
                        Player target = Bukkit.getPlayer(targetName);
                        if (target != null) {
                            chasseur.shot(target);
                        }
                    }
                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez plus tirer !");
                }
            } else {
                player.sendMessage(Messages.not("Chasseur"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle."));
        }
        return false;
    }
}
