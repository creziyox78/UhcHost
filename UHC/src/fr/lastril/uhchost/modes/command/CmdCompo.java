package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.game.tasks.TaskManager;
import fr.lastril.uhchost.inventory.guis.modes.CurrentCompoGui;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
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
        RoleAnnounceMode roleAnnounceMode = null;
        if(mode instanceof RoleAnnounceMode){
            roleAnnounceMode = (RoleAnnounceMode) mode;
        }
        if (mode instanceof RoleMode && mode.getModeManager() != null) {
            if(!mode.getModeManager().compositionHide || (!playerManager.isAlive() && player.isOp())){
                if(GameState.isState(GameState.STARTED) && roleAnnounceMode != null && roleAnnounceMode.isRoleAnnonced(TaskManager.timeGame)){
                    sender.sendMessage("§8§m----------------------------------");
                    for (Camps camp : Camps.values()) {
                        for (PlayerManager playerManagers : pl.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithCamps(camp).stream().filter(PlayerManager::isAlive).collect(Collectors.toList())) {
                            sender.sendMessage(playerManagers.getRole().getCamp().getCompoColor() + playerManagers.getRole().getRoleName() + (player.isOp() && !pl.getPlayerManager(player.getUniqueId()).isAlive() ? " §l(" + playerManagers.getPlayerName() + " | Camps: " + playerManagers.getCamps().name() + ")" : ""));
                        }
                    }
                    sender.sendMessage("§8§m----------------------------------");
                }  else {
                    player.closeInventory();
                    new CurrentCompoGui(UhcHost.getInstance()).open(player);
                }
            } else {
                player.sendMessage(Messages.error("La composition est cachée !"));
            }

        }
        return false;
    }
}
