package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdCompo implements ModeSubCommand {

    private final UhcHost pl;

    private final List<Camps> listSoloCamp = Arrays.asList(Camps.LOUP_GAROU_BLANC, Camps.COUPLE, Camps.ASSASSIN, Camps.TRUBLION, Camps.ANGE);

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
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        Mode mode = pl.gameManager.getModes().getMode();
        if (mode instanceof RoleMode && mode.getModeManager() != null) {
            if(!mode.getModeManager().compositionHide || (!playerManager.isAlive() && player.isOp())){
                sender.sendMessage("§8§m----------------------------------");
                for (Camps camp : Camps.values()) {
                    for (PlayerManager PlayerManagers : mode.getModeManager().getPlayerManagersWithCamps(camp)) {
                        if (PlayerManagers.isAlive() && !listSoloCamp.contains(PlayerManagers.getCamps())) {
                            sender.sendMessage(camp.getCompoColor() + PlayerManagers.getRole().getRoleName() + (player.isOp() && !pl.getPlayerManager(player.getUniqueId()).isAlive() ? " §l(" + PlayerManagers.getPlayerName() + " | Camps: " + PlayerManagers.getCamps().name() + ")" : ""));
                        } else {
                            if(PlayerManagers.isAlive())
                                sender.sendMessage("§6"+PlayerManagers.getRole().getRoleName() + (player.isOp() && !pl.getPlayerManager(player.getUniqueId()).isAlive()? " §l(" + PlayerManagers.getPlayerName() + " | Camps: " + PlayerManagers.getCamps().name() + ")" : ""));
                        }
                    }
                    if (!mode.getModeManager().getPlayerManagersWithCamps(camp).isEmpty())
                        sender.sendMessage(" ");
                }
                sender.sendMessage("§8§m----------------------------------");
            } else {
                player.sendMessage(Messages.error("La composition est cachée !"));
            }

        }
        return false;
    }
}
