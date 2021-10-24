package fr.lastril.uhchost.modes;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public abstract class Mode {

    public Modes mode;

    public Mode(Modes mode) {
        this.mode = mode;
    }

    public abstract void tick(int timer);
    public abstract void onPvp();
    public abstract void onBorder();
    public abstract void onTeleportation();
    public abstract void onNewEpisode();
    public abstract void onDeath(Player player, Player killer);
    public abstract boolean isScheduledDeath();
    public abstract boolean isEpisodeMode();
    public abstract void onDamage(Player target, Player damager);

    public boolean isCancelDamage(EntityDamageByEntityEvent event) {
        return false;
    }

    public abstract void checkWin();

    public abstract ModeManager getModeManager();

}
