package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Ino;
import fr.maygo.uhc.obj.PlayerManager;
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
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Ino) {
                Ino ino = (Ino) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (args.length == 2) {
                        String playerName = args[1];
                        Player target = Bukkit.getPlayer(playerName);
                        if (target != null) {
                            PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                            if (targetPlayerManager.hasRole()) {
                                if (ino.getTransfered() == null) {
                                    ino.setTransfered(target.getUniqueId());
                                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                                        narutoRole.usePower(PlayerManager);
                                    }

                                    player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous avez bien transféré " + target.getName() + ", il pourra écrire un message lors de sa mort.");
                                    target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Ino a utilisé son transfer sur vous, vous pourrez écrire à tout le monde lors de votre mort.");
                                } else {
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
