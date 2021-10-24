package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Salvateur;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSalvation implements ModeSubCommand {

    private final UhcHost pl;

    public CmdSalvation(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "salvation";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        if(!playerManager.hasRole() || !playerManager.isAlive()){
            return false;
        }
        if(playerManager.getRole() instanceof Salvateur){
            Salvateur salvateur = (Salvateur) playerManager.getRole();
            if(!salvateur.isSalvate()){
                if (args.length == 2) {
                    String targetName = args[1];
                    Player target = Bukkit.getPlayer(targetName);
                    if(target != null){
                        PlayerManager targetManager = pl.getPlayerManager(target.getUniqueId());
                        if(targetManager.isAlive() && targetManager.hasRole()){
                            if(!targetManager.getWolfPlayerManager().isSalvation()){
                                salvateur.setSalvate(true);
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§9Vous venez de protéger " + targetName);
                                target.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§bLe Salvateur vient de vous protéger, vous obtenez Résistance I ainsi que NoFall pendant les 20 prochaines minutes.");
                                target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*60*20, 0, false, false));
                                Bukkit.getScheduler().runTaskLater(pl, () -> targetManager.getWolfPlayerManager().setSalvation(false), 20*60*20);
                                targetManager.getWolfPlayerManager().setSalvation(true);
                            } else {
                                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cCe joueur ne peut pas être ciblé !");
                            }
                        } else {
                            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§cCe joueur ne fait pas partie de la partie !");
                        }
                    }
                }
            }
        }
        return false;
    }
}
