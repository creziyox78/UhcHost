package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdCompo implements ModeSubCommand {

    private final UhcHost pl;

    public CmdCompo(UhcHost pl) {
        this.pl = pl;
    }

    @Override
    public String getSubCommandName() {
        return "compo";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Mode mode = pl.gameManager.getModes().getMode();
        if(mode instanceof RoleMode && mode.getModeManager() != null){
            sender.sendMessage("§8§m----------------------------------");
            for (Camps camp : Camps.values()) {
                for (PlayerManager joueurs : mode.getModeManager().getJoueursWithCamps(camp)) {
                    if (joueurs.isAlive()) {
                        sender.sendMessage(camp.getCompoColor() + joueurs.getRole().getRoleName() + (player.isOp() ? " §l(" + joueurs.getPlayerName() + " | Camps: " + joueurs.getCamps().name() + ")" : ""));
                    }
                }
                if (!mode.getModeManager().getJoueursWithCamps(camp).isEmpty())
                    sender.sendMessage(" ");
            }
            sender.sendMessage("§8§m----------------------------------");
        }
        return false;
    }
}
