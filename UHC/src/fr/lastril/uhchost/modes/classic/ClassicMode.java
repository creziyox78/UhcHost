package fr.lastril.uhchost.modes.classic;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class ClassicMode extends Mode {

    private final UhcHost pl;

    public ClassicMode() {
        super(Modes.CLASSIC);
        this.pl = UhcHost.getInstance();
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
        if(player.isOnline()){
            Player onlinePlayer = player.getPlayer();
            Location deathLocation = onlinePlayer.getLocation().clone();
            kill(player, onlinePlayer.getInventory().getContents(), onlinePlayer.getInventory().getArmorContents(), killer, deathLocation);
        }

    }

    public void kill(OfflinePlayer player, ItemStack[] items, ItemStack[] armors, Player killer,
                     Location deathLocation) {
        PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
        playerManager.setAlive(false);

        playerManager.setItems(playerManager.getPlayer().getInventory().getContents());
        playerManager.setArmors(playerManager.getPlayer().getInventory().getArmorContents());
        /* DROPING INVENTORY */
        System.out.println("Droping inventory !");
        pl.getInventoryUtils().dropInventory(deathLocation, items, armors);

        System.out.println("Set spectator !");
        if (playerManager.getPlayer() != null) {
            Player onlinePlayer = playerManager.getPlayer();
            onlinePlayer.setGameMode(GameMode.SPECTATOR);
            onlinePlayer.getInventory().clear();
        }


        System.out.println("On Kill Role !");
        if(killer != null){
            PlayerManager playerManagerKiller = pl.getPlayerManager(killer.getUniqueId());
            playerManagerKiller.addKill(player.getUniqueId());
        }
        checkWin();
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
        if (this.pl.teamUtils.getPlayersPerTeams() != 1 && !this.pl.gameManager.hasScenario(Scenarios.ONLYONEWINNER.getScenario())) {
            if (this.pl.scoreboardUtil.getBoard().getTeams().size() == 1) {
                Team winner = null;
                for (Team team : this.pl.scoreboardUtil.getBoard().getTeams())
                    winner = team;
                win(winner);
            }
        } else if (this.pl.getPlayerManagerAlives().size() == 1) {
            Player winner = this.pl.getPlayerManagerAlives().get(0).getPlayer();
            win(winner);
        }
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

    public void win(Player winner) {
        this.pl.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        if(winner != null){
            Bukkit.broadcastMessage(I18n.tl("winOfPlayer", winner.getName()));
            doParticle(winner.getLocation(), Effect.HEART, 4.0D, 10.0D, 0.5D, 1.0D, 1000.0D, 20D, 100.0D, 1.0D);
        } else {
            Bukkit.broadcastMessage("§7Egalité");
        }



        Bukkit.broadcastMessage(I18n.tl("rebootSoon"));

        Bukkit.getScheduler().runTaskLater(this.pl, () -> {
            if (this.pl.getConfig().getBoolean("bungeecord")) {
                if (this.pl.getConfig().getString("server-redirection") != null && !this.pl
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.pl.getConfig().getString("server-redirection")));
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
    }

    public void win(Team winner) {
        this.pl.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        Bukkit.broadcastMessage(I18n.tl("winOfTeam", winner.getName()));
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        Bukkit.getScheduler().runTaskLater(this.pl, () -> {
            if (this.pl.getConfig().getBoolean("bungeecord")) {
                if (this.pl.getConfig().getString("server-redirection") != null && !this.pl
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.pl.getConfig().getString("server-redirection")));
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
    }

    public void doParticle(final Location center, final Effect effect, double curve, double radius, double radiusChange,
                           double height, double particles, double delay, double amount, double separation) {
        center.add(0.0D, 0.25D, 0.0D);
        for (int i = 0; i < amount; i++) {
            double degrees;
            for (degrees = i * separation; degrees <= 360.0D * curve; degrees += 360.0D / particles) {
                final double y = degrees / 360.0D * curve / height;
                final double x = Math.cos(degrees) * (radius - radiusChange * y);
                final double z = Math.sin(degrees) * (radius - radiusChange * y);
                if (radius - radiusChange * y >= 0.0D) {
                    new BukkitRunnable() {
                        public void run() {
                            center.getWorld().playEffect(center.clone().add(x, y, z), effect, 0);
                        }
                    }.runTaskLater(this.pl, (long) Math.floor(degrees / 360.0D / particles * delay));
                }
            }
        }
    }
}
