package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Garde;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdGarde implements ModeSubCommand {

    private final UhcHost main;

    public CmdGarde(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "garde";
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
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Garde){
            Garde garde = (Garde) playerManager.getRole();
            if(!garde.hasProtect()){
                if(args.length == 2){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null && target != player){
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        if(!garde.alreayProtected(targetManager)){
                            if(wolfPlayerManager.isSarbacaned()){
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                                return false;
                            }
                            targetManager.getWolfPlayerManager().setProtect(true);
                            garde.addProtected(targetManager);
                            garde.setProtect(true);
                            Bukkit.getScheduler().runTaskLater(main, () -> {
                                targetManager.getWolfPlayerManager().setProtect(false);
                            }, 20*60*10);
                            target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bLe garde vient de vous protéger pendant les 10 prochaines minutes.");
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous avez bien protéger " + targetName + ".");
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe joueur a déjà eu votre protection auparavant.");
                        }
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cCe n'est pas possible sur ce joueur.");
                    }
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.NOT_NOW.getMessage());
            }
        } else {
            player.sendMessage(Messages.not("Garde"));
        }
        return false;
    }
}
