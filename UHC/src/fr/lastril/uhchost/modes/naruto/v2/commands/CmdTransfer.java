package fr.lastril.uhchost.modes.naruto.v2.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdTransfer implements ModeSubCommand {

    private final UhcHost main;

    public CmdTransfer(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "transfer";
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
            if (joueur.getRole() instanceof Ino) {
                Ino ino = (Ino) joueur.getRole();
                if (!narutoV2Manager.isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetJoueur = main.getPlayerManager(target.getUniqueId());
                            if (targetJoueur.hasRole()) {
                                if (ino.getTransfered() == null) {
                                    ino.setTransfered(target.getUniqueId());
                                    if (joueur.getRole() instanceof NarutoV2Role) {
                                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                                        narutoRole.usePower(joueur);
                                    }
                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez bien transféré "+target.getName()+", il pourra écrire un message lors de sa mort.");
                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Ino a utilisé son transfer sur vous, vous pourrez écrire à tout le monde lors de votre mort.");
                                    ino.usePower(joueur);
                                    ino.usePowerSpecific(joueur, "/ns transfer");
                                }else{
                                    player.sendMessage(Messages.error("Vous avez déjà utilisé votre pouvoir"));
                                    return false;
                                }
                            } else {
                                player.sendMessage(Messages.NOT_INGAME.getMessage());
                                return false;
                            }
                        } else {
                            player.sendMessage(Messages.UNKNOW_PLAYER.getMessage());
                            return false;
                        }
                    }
                } else {
                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
                    return false;
                }
            } else {
                player.sendMessage(Messages.not("Ino"));
                return false;
            }
        } else {
            player.sendMessage(Messages.error("Vous n'avez pas de rôle !"));
            return false;
        }
        return false;
    }
}
