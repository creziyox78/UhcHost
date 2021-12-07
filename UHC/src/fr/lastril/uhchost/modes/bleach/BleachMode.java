package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.RoleAnnounceMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BleachMode extends Mode implements ModeConfig, RoleAnnounceMode, ModeCommand {

    private final UhcHost main;
    private final BleachManager bleachManager;
    private int announceRoles = 30;

    public BleachMode() {
        super(Modes.BLEACH);
        this.main = UhcHost.getInstance();
        this.bleachManager = new BleachManager(main);
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
    public void onDeath(Player player, Player killer) {

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
        return bleachManager;
    }

    @Override
    public void onNight() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public IQuickInventory getGui() {
        return null;
    }

    @Override
    public int getRoleAnnouncement() {
        return announceRoles;
    }

    @Override
    public void setRoleAnnouncement(int roleAnnouncement) {
        announceRoles = roleAnnouncement;
    }

    @Override
    public String getCommandName() {
        return "bl";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList();
    }
}
