package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.voyante.Voyante;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdVoir implements ModeSubCommand {

    private final UhcHost pl;

    public CmdVoir(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "voir";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if(!playerManager.hasRole() || !playerManager.isAlive()){
            return false;
        }
        if(playerManager.getRole() instanceof Voyante){
            Voyante voyante = (Voyante) playerManager.getRole();
            if(voyante.canSeeRole()){
                if (args.length == 2) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null){
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if(targetManager.isAlive() && targetManager.hasRole()){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§bVous venez d'espionner " + targetName + ". Ce joueur est: " + targetManager.getRole().getRoleName());
                            voyante.setSeeRole(false);
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cCe joueur n'est pas/plus dans la partie !");
                        }
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cCe joueur n'est pas en ligne.");
                    }
                } else {
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§6Précisez un joueur.");
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cVous ne pouvez pas utiliser votre pouvoir pour le moment.");
            }
        }
        return false;
    }
}
