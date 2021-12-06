package fr.lastril.uhchost.modes.lg.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdList implements ModeSubCommand {

    private final UhcHost main;

    public CmdList(UhcHost main) {
        this.main = main;
    }


    @Override
    public String getSubCommandName() {
        return "list";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof RealLG
                || playerManager.getWolfPlayerManager().isInfected()
                || (playerManager.getWolfPlayerManager().isZizanied()
                && playerManager.getWolfPlayerManager().isZizanied(Camps.LOUP_GAROU))){
            if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
                LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
                player.sendMessage(loupGarouManager.sendLGList());
            }
        } else {
            player.sendMessage(Messages.LOUP_GAROU_PREFIX.getMessage() + "§cVous ne pouvez pas accèder à la liste des loups-garous !");
        }
        return false;
    }
}
