package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.village.ChefDuVillage;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdCancelVoteChef implements ModeSubCommand {

    private final UhcHost main;

    public CmdCancelVoteChef(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "chef_annule";
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
        LoupGarouManager loupGarouManager = (LoupGarouManager) main.getGamemanager().getModes().getMode().getModeManager();
        if(playerManager.getRole() instanceof ChefDuVillage){
            ChefDuVillage chefDuVillage = (ChefDuVillage) playerManager.getRole();
            if(!chefDuVillage.hasCancelledVote() && !chefDuVillage.isUsed()){
                if(loupGarouManager.mostVoted() == playerManager.getWolfPlayerManager()){
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous êtes celui qui a reçu le plus de vote, vous ne pouvez donc pas annuler les votes !");
                    return false;
                }
                if(wolfPlayerManager.isSarbacaned()){
                    player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                    return false;
                }
                chefDuVillage.setUsed(true);
                chefDuVillage.setCancelledVote(true);
                loupGarouManager.setCancelledVoteChef(true);
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§bVous venez d'annuler le vote du village.");
            } else {
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }
        }
        return false;
    }
}
