package fr.lastril.uhchost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMumble implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            player.sendMessage("§8§m----------------------------------");
            player.sendMessage("§6Adresse : §cokenzai.mumble.gg");
            player.sendMessage("§6Port : §c10001");
            player.sendMessage("§8§m----------------------------------");
        }
        return false;
    }

}
