package fr.lastril.uhchost.modes.bleach.kyoraku;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KyorakuEndDuelEvent extends Event implements Cancellable {
    private final Player winner, loser;

    private static final HandlerList handlers = new HandlerList();
    private final KyorakuDuelManager kyorakuDuelManager;

    public KyorakuEndDuelEvent(KyorakuDuelManager kyorakuDuelManager, Player winner, Player loser) {
        this.winner = winner;
        this.loser = loser;
        this.kyorakuDuelManager = kyorakuDuelManager;
        setCancelled(false);
    }

    public Player getLoser() {
        return loser;
    }

    public Player getWinner() {
        return winner;
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
