package fr.lastril.uhchost.modes.bleach;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Mayuri;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Nemu;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BleachManager extends ModeManager {

    private final UhcHost main;
    private boolean nuitNoir;

    public BleachManager(UhcHost main) {
        this.main = main;
    }

    public void onDeath(OfflinePlayer player, Player killer){
        if(killer != null){
            PlayerManager killerManager = main.getPlayerManager(killer.getUniqueId());
            killerManager.addKill(player.getUniqueId());
        }
        PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
        if(joueur != null){
            if(joueur.getRole() instanceof Nemu){
                Mayuri mayuri = null;
                for(PlayerManager playerManager : main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithRole(Mayuri.class)){
                    if(playerManager.isAlive()){
                        mayuri = (Mayuri) playerManager.getRole();
                    }
                }
                if(mayuri != null){
                    if(!mayuri.hasReanimedNemu()){
                        return;
                    }
                }
                Nemu nemu = (Nemu) joueur.getRole();
                nemu.setDead(false);
            }

            Player onlinePlayer = player.getPlayer();
            if(player.isOnline()){
                joueur.setDeathLocation(onlinePlayer .getLocation());
                joueur.setItems(onlinePlayer .getInventory().getContents());
                joueur.setArmors(onlinePlayer.getInventory().getArmorContents());
            }
            if (joueur.hasRole()) {
                if(!nuitNoir){
                    Bukkit.broadcastMessage("§3§m----------------------------------");
                    Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort, son rôle était "+joueur.getRole().getCamp().getCompoColor()+joueur.getRole().getRoleName()+"§7.");
                    Bukkit.broadcastMessage("§3§m----------------------------------");
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
            } else {
                if(!nuitNoir){
                    Bukkit.broadcastMessage("§3§m----------------------------------");
                    Bukkit.broadcastMessage("§b§l" + player.getName() + "§7 est mort.");
                    Bukkit.broadcastMessage("§3§m----------------------------------");
                }
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if(!nuitNoir){
                    players.playSound(players.getLocation(), Sound.WITHER_DEATH, 1f, 1f);
                }
                PlayerManager playerManager = main.getPlayerManager(players.getUniqueId());
                if(playerManager.isAlive() && playerManager.hasRole()){
                    playerManager.getRole().onPlayerDeath(player.getPlayer());
                    if(killer != null){
                        playerManager.getRole().onKill(player, killer);
                    }
                }

            }

            joueur.setAlive(false);

            /* DROPING INVENTORY */
            System.out.println("Droping inventory !");
            main.getInventoryUtils().dropInventory(joueur.getDeathLocation(), joueur.getItems(), joueur.getArmors());
            main.getGamemanager().getModes().getMode().checkWin();
        }
    }

    public void setNuitNoir(boolean nuitNoir) {
        this.nuitNoir = nuitNoir;
    }

    public boolean isNuitNoir() {
        return nuitNoir;
    }
}
