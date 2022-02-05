package fr.lastril.uhchost.modes.bleach.kyoraku;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KyorakuStartDuelEvent extends Event implements Cancellable {
    private final Player player1, player2;

    private static final HandlerList handlers = new HandlerList();
    private final KyorakuDuelManager kyorakuDuelManager;

    public KyorakuStartDuelEvent(KyorakuDuelManager kyorakuDuelManager, Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.kyorakuDuelManager = kyorakuDuelManager;
        setCancelled(false);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isCancelled() {
        return false;
    }

    public void setCancelled(boolean b) {
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
