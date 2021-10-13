package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.scenario.ActiveScenariosGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRules implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player player = (Player)sender;
			
			if(cmd.getName().equalsIgnoreCase("scenarios")) {
				(new ActiveScenariosGui(player)).show();
				return true;
			}
			
		}
		
		return false;
	}

}
