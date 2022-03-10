package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.solo.Trappeur;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTrapper implements ModeSubCommand {

    private final UhcHost main;

    public CmdTrapper(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "trapper";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        WolfPlayerManager wolfPlayerManager = playerManager.getWolfPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Trappeur){
            Trappeur trappeur = (Trappeur) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null){
                    PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                    if(targetManager.isAlive()){
                        if(!trappeur.canChange()){
                            if(wolfPlayerManager.isSarbacaned()){
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                                return false;
                            }
                            trappeur.setTracked(targetManager);
                            trappeur.setChange(true);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous tracker maintenant " + targetName + " !");
                            target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§2Le Trappeur§2 est désormais sur vos traces.");
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§2Vous avez perdue la trace de§e " + targetManager.getPlayerName() + "§2 !");
                                trappeur.setTracked(null);
                                target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§2Le Trappeur§2 a perdu votre trace !");

                            }, 20*60*10);
                        }
                    }
                }
            }
        }
        return false;
    }
}
