package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdDiscord implements CommandExecutor {

	private final UhcHost main;

	public CmdDiscord(UhcHost main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			TextComponent message = new TextComponent("§7");
			message.addExtra(new TextComponentBuilder("§9§o[Cliquer ici pour rejoindre notre discord]")
					.setClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/SFBxJd7Bcg")
					.setHoverEvent(HoverEvent.Action.SHOW_TEXT, "§9Redirection vers le discord").toText());
			player.sendMessage("§8§m--------------------------------------------------§r");
			player.spigot().sendMessage(message);
			player.sendMessage("§8§m--------------------------------------------------§r");
		}
		return false;
	}

}
