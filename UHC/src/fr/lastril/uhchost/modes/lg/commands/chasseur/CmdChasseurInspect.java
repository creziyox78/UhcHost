package fr.lastril.uhchost.modes.lg.commands.chasseur;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Chasseur;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdChasseurInspect implements ModeSubCommand {

    private final UhcHost main;

    public CmdChasseurInspect(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "chasseur_inspect";
    }

    @Override
    public List<String> getSubArgs() {
        return main.getPlayerManagerAlives().stream().filter(playerManager -> playerManager.getPlayer() != null).map(PlayerManager::getPlayerName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
        if(playerManager.hasRole()){
            if(playerManager.getRole() instanceof Chasseur){
                Chasseur chasseur = (Chasseur) playerManager.getRole();
                if(args.length == 2){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if (target != null) {
                        if(wolfPlayerManager.isSarbacaned()){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                            return false;
                        }
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        chasseur.sendInvestigateMessage(player, targetManager);
                    }
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
