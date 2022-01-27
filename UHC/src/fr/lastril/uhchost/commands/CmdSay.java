package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSay implements CommandExecutor {

	private final UhcHost main;

	public CmdSay(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (main.gameManager.getHost() == player.getUniqueId() || main.gameManager.isCoHost(player)) {
				if (args.length > 0) {
					String message = "";
					for (int i = 0; i < args.length; i++) {
						message += args[i] + " ";
					}
					Bukkit.broadcastMessage(" ");
					Bukkit.broadcastMessage("§6§lHOTE "+player.getName()+" §e: §b" + message);
					Bukkit.broadcastMessage(" ");
				} else {
					player.sendMessage(Messages.use("/say <message>"));
				}
			} else {
				player.sendMessage(Messages.NOT_PERM.getMessage());
			}
		} else {
			sender.sendMessage(Messages.ONLY_PLAYER.getMessage());
		}
		return false;
	}

}
