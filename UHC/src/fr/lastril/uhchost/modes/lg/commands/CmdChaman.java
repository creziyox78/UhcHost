package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Chaman;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdChaman implements ModeSubCommand {

    private final UhcHost main;

    public CmdChaman(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "chaman";
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
        if(playerManager.getRole() instanceof Chaman){
            Chaman chaman = (Chaman) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null && target != player){
                    PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                    if(!chaman.containsPlayerSpec(targetManager)){
                        int value = UhcHost.getRANDOM().nextInt(2);
                        if(value == 1)
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§Voici le pseudo du joueur: " + targetManager.getPlayerName());
                        else
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eCe joueur possède le rôle: " + targetManager.getRole().getRoleName());
                        chaman.addPlayerSpec(targetManager);
                        player.setMaxHealth(player.getMaxHealth() - 2D);
                    } else {
                        player.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre pouvoir sur ce joueur."));
                    }

                } else {
                    player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre pouvoir sur ce joueur."));
                }
            }
        } else {
            player.sendMessage(Messages.not("Chaman"));
        }
        return false;
    }
}
