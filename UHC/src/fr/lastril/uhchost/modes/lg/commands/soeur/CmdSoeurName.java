package fr.lastril.uhchost.modes.lg.commands.soeur;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.roles.village.Soeur;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdSoeurName implements ModeSubCommand {

    private final UhcHost main;

    public CmdSoeurName(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "soeur_pseudo";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if (playerManager.getRole() instanceof Soeur) {
            Soeur soeur = (Soeur) playerManager.getRole();
            if(soeur.isOtherDead()){
                soeur.setOtherDead(false);
                UhcHost.debug("Other soeur choose name of killer : " + soeur.getPlayerKiller().getPlayerName());
                player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§eVoici le pseudo du tueur de votre soeur: " + soeur.getPlayerKiller().getPlayerName());
            } else {
                player.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
            }
        } else {
            player.sendMessage(Messages.not("Soeur"));
        }
        return false;
    }
}
