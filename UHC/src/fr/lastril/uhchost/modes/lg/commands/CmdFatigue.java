package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Bucheron;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdFatigue implements ModeSubCommand {

    private final UhcHost main;

    public CmdFatigue(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "fatiguer";
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
        if(playerManager.getRole() instanceof Bucheron){
            Bucheron bucheron = (Bucheron) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null && target != player){
                    if(!bucheron.isFatigued()){
                        if(wolfPlayerManager.isSarbacaned()){
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cLa sarcabane de l'Indigène vous empêche d'utiliser votre pouvoir !");
                            return false;
                        }
                        bucheron.setFatigued(true);
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLa fatigue s'est emparé de " + target.getName() + " !");
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5*60, 0, false, false));
                        target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§7La fatigue s'empare de vous...");
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre pouvoir sur ce joueur."));
                }
            }
        } else {
            player.sendMessage(Messages.not("Bûcheron"));
        }
        return false;
    }
}
