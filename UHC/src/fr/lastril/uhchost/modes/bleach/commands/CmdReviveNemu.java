package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Mayuri;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CmdReviveNemu implements ModeSubCommand {

    private final UhcHost main;

    public CmdReviveNemu(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "revive_nemu";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        if(playerManager.getRole() instanceof Mayuri){
            Mayuri mayuri = (Mayuri) playerManager.getRole();
            if(bleachPlayerManager.canUsePower()){
                if(!mayuri.hasReanimedNemu()){
                    mayuri.reviveNemu();
                }
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
            }
        } else {
            player.sendMessage(Messages.not("Mayuri"));
        }
        return false;
    }
}
