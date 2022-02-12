package fr.lastril.uhchost.modes.ds;

import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class DemonSlayerMode extends Mode {

    public DemonSlayerMode() {
        super(Modes.DS);
    }

    @Override
    public void tick(int timer) {

    }

    @Override
    public void onPvp() {

    }

    @Override
    public void onBorder() {

    }

    @Override
    public void onTeleportation() {

    }

    @Override
    public void onNewEpisode() {

    }

    @Override
    public void onDeath(OfflinePlayer player, Player killer) {

    }

    @Override
    public boolean isScheduledDeath() {
        return false;
    }

    @Override
    public boolean isEpisodeMode() {
        return false;
    }

    @Override
    public void onDamage(Player target, Player damager) {

    }

    @Override
    public void checkWin() {

    }

    @Override
    public ModeManager getModeManager() {
        return null;
    }

    @Override
    public void onNight() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public List<Camps> getCamps() {
        return null;
    }

    @Override
    public IQuickInventory getCurrentCompoGui() {
        return null;
    }
}
