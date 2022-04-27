package fr.lastril.uhchost.modes.command;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdInfo implements ModeSubCommand{

    private final UhcHost main;

    public CmdInfo(UhcHost main){
        this.main = main;
    }

    @Override
    public String getSubCommandName() {
        return "info";
    }

    @Override
    public List<String> getSubArgs() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        main.getGamemanager().getModes().getMode().sendInfo(player);
        return false;
    }
}
