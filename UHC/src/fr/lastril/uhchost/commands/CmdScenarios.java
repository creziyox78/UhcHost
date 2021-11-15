package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.inventory.guis.rules.RulesGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdScenarios implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {

			Player player = (Player)sender;

			new RulesGui().open(player);
		}
		return false;
	}

}
