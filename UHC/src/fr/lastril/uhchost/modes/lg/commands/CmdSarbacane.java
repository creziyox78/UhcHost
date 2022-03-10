package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Indigene;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSarbacane implements ModeSubCommand {

    private final UhcHost main;

    public CmdSarbacane(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "sarbacane";
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
        if(playerManager.getRole() instanceof Indigene){
            Indigene indigene = (Indigene) playerManager.getRole();
            if(args.length == 2){
                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);
                if(target != null && target != player){
                    if(!indigene.hasSarbacaned()){
                        indigene.setSarbacaned(true);
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aLa flèche de votre sarbacane a atteint sa cible,§e "+ target.getName() +"§2 s'endort et ne peux plus utilisez ces pouvois ou voté jusqu'au prochain épisode !");
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        targetManager.getWolfPlayerManager().setSarbacaned(true);
                        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 250, false, false));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 250, false, false));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*5, 250, false, false));
                        target.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§aL'indigène vient de vous endormir...");
                    } else {
                        player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
                } else {
                    player.sendMessage(Messages.error("Vous ne pouvez pas utiliser votre pouvoir sur ce joueur."));
                }
            }
        } else {
            player.sendMessage(Messages.not("Indigène"));
        }
        return false;
    }
}
