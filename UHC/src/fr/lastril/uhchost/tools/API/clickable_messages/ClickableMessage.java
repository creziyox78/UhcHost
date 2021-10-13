package fr.lastril.uhchost.tools.API.clickable_messages;

import java.util.function.Consumer;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ClickableMessage {

	private final int id;
	private final Player player;
	private final Consumer<Player> onClick;
	
	public ClickableMessage(Player player, Consumer<Player> onClick, String message) {
		this(player, onClick, message, null);
	}
	
	public ClickableMessage(Player player, Consumer<Player> onClick, String message, String hover) {
		this.id = UhcHost.getRANDOM().nextInt(50000);
		this.player = player;
		this.onClick = onClick;
		
		TextComponent text = new TextComponent(message);
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""+id));
		if(hover != null) {
			text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		}
		
		player.spigot().sendMessage(text);
		
		ClickableMessageManager.addMessage(this);
	}

	public int getId() {
		return this.id;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Consumer<Player> getOnClick() {
		return this.onClick;
	}
	
}
