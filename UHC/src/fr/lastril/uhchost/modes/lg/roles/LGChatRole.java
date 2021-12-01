package fr.lastril.uhchost.modes.lg.roles;

public interface LGChatRole {

    boolean canSee();

    boolean canSend();

    boolean sendPlayerName();
}
