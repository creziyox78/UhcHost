package fr.lastril.uhchost.modes.bleach.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFaible;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFort;
import fr.lastril.uhchost.modes.bleach.ceros.CeroMoyen;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdCero implements ModeSubCommand {

    private final UhcHost main;

    public CmdCero(UhcHost main) {
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "cero";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if (!playerManager.hasRole() || !playerManager.isAlive()) {
            return false;
        }
        new CeroFort().giveCero(player);
        new CeroMoyen().giveCero(player);
        new CeroFaible().giveCero(player);
        return false;
    }
}
