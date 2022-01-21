package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Isane;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdHeal implements ModeSubCommand {

    private final UhcHost main;

    public CmdHeal(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "heal";
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
        if(playerManager.getRole() instanceof Isane){
            Isane isane = (Isane) playerManager.getPlayer();
            if(playerManager.getRoleCooldownHeal() <= 0){
                if(args.length == 3){
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null){
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        if(targetManager.isAlive()){
                            isane.setHealedManager(targetManager);
                            if(target.hasPotionEffect(PotionEffectType.REGENERATION))
                                target.removePotionEffect(PotionEffectType.REGENERATION);
                            if(target.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                                target.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*20, 1, false, false));
                            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false));
                            player.sendMessage("§aVous offrez votre bénédiction à " + targetName +".");
                            target.sendMessage("§aIsane vous a offert sa bénédiction.");
                            playerManager.setRoleCooldownHeal(5*60);
                        } else {
                            player.sendMessage(Messages.error("Ce joueur n'est pas en vie !"));
                        }
                    } else {
                        player.sendMessage(Messages.error("Ce joueur n'est pas en ligne."));
                    }
                } else {
                    player.sendMessage(Messages.use("/b heal <pseudo>"));
                }
            } else {
                player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownHeal()));
            }


        }
        return false;
    }
}
