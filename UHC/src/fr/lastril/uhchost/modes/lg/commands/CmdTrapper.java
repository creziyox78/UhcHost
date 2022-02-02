package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.lg.LoupGarouPisteur;
import fr.lastril.uhchost.modes.lg.roles.village.Trappeur;
import fr.lastril.uhchost.player.PlayerManager;
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
                            trappeur.setTracked(targetManager);
                            trappeur.setChange(true);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous tracker maintenant " + targetName + " !");
                        }
                    }
                }
            }
        }
        if(playerManager.getRole() instanceof LoupGarouPisteur){
            LoupGarouPisteur loupGarouPisteur = (LoupGarouPisteur) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null){
                    PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                    if(targetManager.isAlive()){
                        if(!loupGarouPisteur.canChange()){
                            loupGarouPisteur.setTracked(targetManager);
                            loupGarouPisteur.setChange(true);
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous tracker maintenant " + targetName + " !");
                        }
                    }
                }
            }
        }
        return false;
    }
}
