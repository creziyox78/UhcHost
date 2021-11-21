package fr.lastril.uhchost.tools.API;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentBuilder {

    private TextComponent textComponent;

    public TextComponentBuilder(String text){
        textComponent = new TextComponent(text); 
    }

    public TextComponentBuilder setHoverEvent(HoverEvent.Action action, String str){
        ComponentBuilder componentBuilder = new ComponentBuilder(str);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()));
        return this;
    }

    public TextComponentBuilder setClickEvent(ClickEvent.Action action, String str){
        textComponent.setClickEvent(new ClickEvent(action, str));
        return this;
    }

    public TextComponentBuilder setBold(boolean bold){
        textComponent.setBold(bold);
        return this;
    }

    public TextComponentBuilder setText(String text){
        textComponent.setText(text);
        return this;
    }

    public TextComponentBuilder setItalic(boolean italic){
        textComponent.setItalic(italic);
        return this;
    }

    public TextComponentBuilder setColor(ChatColor color){
        textComponent.setColor(color);
        return this;
    }

    public TextComponentBuilder setObfuscated(boolean obfuscated){
        textComponent.setObfuscated(obfuscated);
        return this;
    }

    public TextComponentBuilder setUnderlined(boolean underlined){
        textComponent.setUnderlined(underlined);
        return this;
    }

    public TextComponentBuilder setStrikeTrough(boolean strikethrough){
        textComponent.setStrikethrough(strikethrough);
        return this;
    }

    public TextComponent toText(){
        return textComponent;
    }

}