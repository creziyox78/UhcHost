package fr.lastril.uhchost.modes.naruto;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.modes.naruto.v2.NarutoGUI;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.command.ModeCommand;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Roles;
import fr.lastril.uhchost.modes.naruto.v2.biju.BijuManager;
import fr.lastril.uhchost.modes.naruto.v2.commands.hokage.CmdBoost;
import fr.lastril.uhchost.modes.naruto.v2.commands.hokage.CmdReveal;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class NarutoV2 extends Mode implements ModeConfig, RoleMode<NarutoV2Role>, ModeCommand {

    private final UhcHost main;
    private final NarutoV2Config narutoV2Config;

    public NarutoV2() {
        super(Modes.NARUTO_V2);
        main = UhcHost.getInstance();
        this.narutoV2Config = new NarutoV2Config(50, true, 20);
    }


    @Override
    public IQuickInventory getGUI() {
        return new NarutoGUI(main, narutoV2Config);
    }

    @Override
    public void tick(int timer) {
        if (timer == 1 && narutoV2Config.isBiju()) {
            main.getNarutoV2Manager().setBijuManager(new BijuManager(main));
        }
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
    public void onDeath(Player player, Player killer) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        playerManager.setAlive(false);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
        }
        if (playerManager.hasRole()) {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était " + playerManager.getRole().getCamp().getCompoColor() + playerManager.getRole().getRoleName() + "§7.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.spigot().respawn();
                }
            }.runTaskLater(main, 2);

            new BukkitRunnable() {

                @Override
                public void run() {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }.runTaskLater(main, 5);
        } else {
            Bukkit.broadcastMessage("§3§m----------------------------------");
            Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort.");
            Bukkit.broadcastMessage("§3§m----------------------------------");
        }
    }

    @Override
    public boolean isScheduledDeath() {

        return false;
    }

    @Override
    public boolean isEpisodeMode() {

        return true;
    }


    @Override
    public void onNewEpisode() {


    }

    @Override
    public void giveRoles() {

    }

    @Override
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        for (NarutoV2Roles role : NarutoV2Roles.values()) {
            try {
                roles.add(role.getRole().newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return roles;
    }

    @Override
    public String getDocLink() {
        return "https://docs.mcatlantis.fr";
    }

    @Override
    public String getCommandName() {
        return "narutos";
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        List<ModeSubCommand> subCommands = new ArrayList<>();

        this.getRoles().stream().filter(role -> role instanceof RoleCommand).map(role -> {
            return ((RoleCommand) role).getSubCommands();
        }).forEach(subCommands::addAll);

        subCommands.add(new CmdBoost(main));
        subCommands.add(new CmdReveal(main));
        return subCommands;
    }

    @Override
    public void onDamage(Player target, Player damager) {
        main.getNarutoV2Manager().onDamage(target, damager);
    }
}
