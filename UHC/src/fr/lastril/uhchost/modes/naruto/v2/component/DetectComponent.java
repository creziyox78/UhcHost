package fr.lastril.uhchost.modes.naruto.v2.component;

import fr.lastril.uhchost.tools.API.TextComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;

public class DetectComponent extends TextComponentBuilder {

    public DetectComponent(String playerName) {
        super(ChatColor.GOLD+playerName+" ");
        super.setHoverEvent(HoverEvent.Action.SHOW_TEXT, "§6Traquer "+playerName);
        super.setClickEvent(ClickEvent.Action.RUN_COMMAND, "/ns detect "+playerName);
    }
}
