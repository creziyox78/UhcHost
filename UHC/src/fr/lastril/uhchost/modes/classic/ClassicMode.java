package fr.lastril.uhchost.modes.classic;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Mode;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.scenario.Scenarios;
import fr.lastril.uhchost.tools.API.BungeeAPI;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class ClassicMode extends Mode {

    private final UhcHost main;

    public ClassicMode() {
        super(Modes.CLASSIC);
        this.main = UhcHost.getInstance();
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
        if(killer != null){
            PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
            killerManager.addKill(player.getUniqueId());
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            if(player.getPlayer() != null && player.isOnline()){
                WorldUtils.spawnFakeLightning(players, player.getPlayer().getLocation(), true);
            }

        }

        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur != null){
            Player onlinePlayer = player.getPlayer();
            if(player.isOnline()){
                joueur.setDeathLocation(onlinePlayer .getLocation());
                joueur.setItems(onlinePlayer .getInventory().getContents());
                joueur.setArmors(onlinePlayer.getInventory().getArmorContents());
            }

            joueur.setAlive(false);
            if(main.teamUtils.getTeam(player.getPlayer()) != null){
                Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + main.teamUtils.getTeam(player.getPlayer()).getPrefix() + player.getName() + " §cest mort.");
            } else if(main.teamUtils.getTaupes(player.getPlayer()) != null){
                if(main.teamUtils.getTaupes(player.getPlayer()).getTeam() != null){
                    Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + main.teamUtils.getTaupes(player.getPlayer()).getTeam().getPrefix() + player.getName() + " §cest mort.");
                }
            } else {
                Bukkit.broadcastMessage(Messages.PREFIX_WITH_ARROW.getMessage() + player.getName() + " §cest mort.");
            }


            new BukkitRunnable() {

                @Override
                public void run() {
                    onlinePlayer.spigot().respawn();
                }
            }.runTaskLater(main, 5);

            new BukkitRunnable() {

                @Override
                public void run() {
                    onlinePlayer.setGameMode(GameMode.ADVENTURE);
                    onlinePlayer.setGameMode(GameMode.SPECTATOR);
                    onlinePlayer.teleport(joueur.getDeathLocation());
                }
            }.runTaskLater(main, 10);
            /* DROPING INVENTORY */
            System.out.println("Droping inventory !");
            main.getInventoryUtils().dropInventory(joueur.getDeathLocation(), joueur.getItems(), joueur.getArmors());
            checkWin();
        }





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
        if (this.main.teamUtils.getPlayersPerTeams() != 1 && !this.main.gameManager.hasScenario(Scenarios.ONLYONEWINNER.getScenario())) {
            if (this.main.scoreboardUtil.getBoard().getTeams().size() == 1) {
                Team winner = null;
                for (Team team : this.main.scoreboardUtil.getBoard().getTeams())
                    winner = team;
                win(winner);
            }
        } else if (this.main.getPlayerManagerAlives().size() == 1) {
            Player winner = this.main.getPlayerManagerAlives().get(0).getPlayer();
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
        this.main.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        if(winner != null){
            Bukkit.broadcastMessage(I18n.tl("winOfPlayer", winner.getName()));
            doParticle(winner.getLocation(), Effect.HEART, 4.0D, 10.0D, 0.5D, 1.0D, 1000.0D, 20D, 100.0D, 1.0D);
        } else {
            Bukkit.broadcastMessage("§7Egalité");
        }



        Bukkit.broadcastMessage(I18n.tl("rebootSoon"));

        Bukkit.getScheduler().runTaskLater(this.main, () -> {
            if (this.main.getConfig().getBoolean("bungeecord")) {
                if (this.main.getConfig().getString("server-redirection") != null && !this.main
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.main.getConfig().getString("server-redirection")));
            }

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
        }, 30 * 20L);
    }

    public void win(Team winner) {
        this.main.gameManager.setDamage(false);
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        Bukkit.broadcastMessage(I18n.tl("winOfTeam", winner.getName()));
        Bukkit.broadcastMessage(I18n.tl("endGame"));
        Bukkit.getScheduler().runTaskLater(this.main, () -> {
            if (this.main.getConfig().getBoolean("bungeecord")) {
                if (this.main.getConfig().getString("server-redirection") != null && !this.main
                        .getConfig().getString("server-redirection").equalsIgnoreCase("null"))
                    Bukkit.getOnlinePlayers().forEach(p -> BungeeAPI.ConnectBungeeServer(p,
                            this.main.getConfig().getString("server-redirection")));
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
                    }.runTaskLater(this.main, (long) Math.floor(degrees / 360.0D / particles * delay));
                }
            }
        }
    }
}
