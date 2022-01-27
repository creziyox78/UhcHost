package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSetGroupes implements CommandExecutor {

	private final UhcHost main;
	
	public CmdSetGroupes(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (main.gameManager.getHost() == player.getUniqueId() || player.isOp() || main.gameManager.isCoHost(player)) {
				if (args.length > 0) {
					try {
						int newGroupes = Integer.parseInt(args[0]);
						main.gameManager.setGroupes(newGroupes);
						Bukkit.getOnlinePlayers().forEach(player1 -> main.gameManager.rappelGroupes(player1));
						return true;
					} catch (Exception e) {
						player.sendMessage(Messages.UNVALID_NUMBER.getMessage());
						return false;
					}
				} else {
					player.sendMessage(Messages.use("/setgroupes <nombres>"));
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
