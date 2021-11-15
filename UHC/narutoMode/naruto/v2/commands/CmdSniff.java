package fr.lastril.uhchost.modes.naruto.v2.commands;

;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.crafter.command.ModeSubCommand;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kiba;
import fr.maygo.uhc.modes.naruto.v2.tasks.SniffTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSniff implements ModeSubCommand {

    private final UhcHost main;

    public CmdSniff(UhcHost main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager PlayerManager = main.getPlayerManager(player.getUniqueId());
        if (!PlayerManager.isAlive()) {
            player.sendMessage(Messages.error("Vous n'êtes plus vivant."));
            return false;
        }
        if (PlayerManager.hasRole()) {
            if (PlayerManager.getRole() instanceof Kiba) {
                Kiba kiba = (Kiba) PlayerManager.getRole();
                if (!main.getNarutoV2Manager().isInSamehada(player.getUniqueId())) {
                    if (kiba.isCanSniff()) {
                        if (args.length == 2) {
                            String playerName = args[1];
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                PlayerManager targetPlayerManager = main.getPlayerManager(target.getUniqueId());
                                if (targetPlayerManager.isAlive()) {
                                    if (targetPlayerManager.getPlayer().getLocation().distance(player.getLocation()) <= kiba.getDistanceSniff()) {
                                        kiba.setPlayerSniffed(targetPlayerManager.getId());
                                        kiba.setCanSniff(false);
                                        new SniffTask(main, kiba, target.getUniqueId()).runTaskTimer(main, 0, 20);
                                        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Kiba traque désormais " + target.getName() + " !");
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return false;
    }

    @Override
    public String getSubCommandName() {
        return "sniff";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }


}
