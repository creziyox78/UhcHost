package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Chaman;
import fr.lastril.uhchost.modes.lg.roles.village.ChefDuVillage;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdEnquete implements ModeSubCommand {

    private final UhcHost main;

    public CmdEnquete(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "enquete";
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
        if(playerManager.getRole() instanceof ChefDuVillage){
            ChefDuVillage chefDuVillage = (ChefDuVillage) playerManager.getRole();
            if(!chefDuVillage.hasEnquete()){
                if(args.length == 2){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null && target != player){
                        if(chefDuVillage.getEnqueted() != null){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous devez d'abord terminé l'enquête en cours pour pouvoir en faire une nouvelle.");
                            return false;
                        }
                        if(wolfPlayerManager.isSarbacaned()){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                            return false;
                        }
                        chefDuVillage.setEnqueted(main.getPlayerManager(target.getUniqueId()));
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aVous menez une enquête sur " + target.getName() + ".");
                    } else {
                        player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre pouvoir sur ce joueur."));
                    }
                }
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }

        } else {
            player.sendMessage(Messages.not("Chef du Village"));
        }
        return false;
    }
}
