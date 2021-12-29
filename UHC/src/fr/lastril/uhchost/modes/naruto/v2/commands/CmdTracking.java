package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Shino;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTracking implements ModeSubCommand {

    private final UhcHost main;

    public CmdTracking(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "tracking";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return false;
        NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
        Player player = (Player) sender;
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(!joueur.isAlive()){
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (joueur.hasRole()) {
            if (joueur.getRole() instanceof Shino) {
                Shino shino = (Shino) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                            if (targetJoueur.hasRole()) {
                                if(shino.getLinkeds().containsKey(target.getUniqueId())){
                                    boolean infos = shino.getLinkeds().get(target.getUniqueId());
                                    if(infos){
                                        shino.getLinkeds().put(target.getUniqueId(), false);
                                        shino.getTrackeds().add(target.getUniqueId());
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous traquez maitenant "+target.getName());
                                        shino.usePower(joueur);
                                        shino.usePowerSpecific(joueur, "/ns tracking");
                                    }else{
                                        player.sendMessage(Messages.error("Vous ne pouvez plus utiliser votre commande sur "+target.getName()+" !"));
                                    }
                                }else{
                                    player.sendMessage(Messages.error("Vous n'avez pas infiltré "+target.getName()+" !"));
                                }
                            } else {
                                player.sendMessage(Messages.NOT_INGAME.getMessage());
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Shino"));
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
