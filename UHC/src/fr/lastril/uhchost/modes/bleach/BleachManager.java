package fr.lastril.uhchost.modes.bleach;

import com.avaje.ebeaninternal.api.ClassUtil;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.ModeManager;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.ceros.events.CeroExploseEvent;
import fr.lastril.uhchost.modes.bleach.items.FormLiberer;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.JushiroUkitake;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Mayuri;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Nemu;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.ClassUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.lastril.uhchost.modes.bleach.ceros.CeroType.CEROS_FORT;

public class BleachManager extends ModeManager implements Listener {

    private final UhcHost main;
    private boolean nuitNoir;

    public BleachManager(UhcHost main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
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
            //main.getGamemanager().getModes().getMode().checkWin();
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCeroExplode(CeroExploseEvent event){
        UhcHost.debug("CeroExploseEvent");
        if(!event.isCancelled()){
            UhcHost.debug("CeroExploseEvent is not cancelled");
            switch (event.getCeroType()){
                case CEROS_FORT:
                    Location location = event.getExploseLocation();
                    event.getExploseLocation().getWorld().createExplosion(location.getX(),location.getY(), location.getZ(), 1, false, false);
                    //ClassUtils.fakeExplosion(location, 7);
                    for(Entity entity : event.getExploseLocation().getWorld().getNearbyEntities(location, 7, 7, 7)) {
                        if(entity instanceof Player){
                            Player player = (Player) entity;
                            new BukkitRunnable() {
                                int seconds = 0;
                                @Override
                                public void run() {

                                    player.setFireTicks(20*2);
                                    seconds++;
                                    if(seconds >= 10){
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(main, 0, 20);

                            player.damage(2D*3D);

                        }
                    }
                    break;
                case CEROS_MOYEN:

                    location = event.getExploseLocation();
                    event.getExploseLocation().getWorld().createExplosion(location.getX(),location.getY(), location.getZ(), 1, false, false);
                    //ClassUtils.fakeExplosion(location, 3);
                    for(Entity entity : event.getExploseLocation().getWorld().getNearbyEntities(location, 4, 4, 4)) {
                        if(entity instanceof Player){
                            Player player = (Player) entity;
                            player.damage(2D*2D);
                            ceroMoyenEffectToPlayer(player);
                        }
                    }
                    break;
                case CEROS_FAIBLE:
                    location = event.getExploseLocation();
                    event.getExploseLocation().getWorld().createExplosion(location.getX(),location.getY(), location.getZ(), 0, false, false);
                    //ClassUtils.fakeExplosion(location, 1);
                    for(Entity entity : event.getExploseLocation().getWorld().getNearbyEntities(location, 2, 2, 2)) {
                        if(entity instanceof Player){
                            Player player = (Player) entity;
                            player.damage(2D);
                        }
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void onBreakQuartzBlock(BlockBreakEvent event){
        if(event.getBlock().getType() == Material.QUARTZ_ORE){
            if(event.getBlock().getWorld() == Bukkit.getWorld("soulsociety_the_end")){
                Player player = event.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                if(playerManager.getRole() instanceof ArrancarRole){
                    ArrancarRole arrancarRole = (ArrancarRole) playerManager.getRole();
                    if(bleachPlayerManager.getNbQuartzMined() < arrancarRole.getNbQuartz()){
                        bleachPlayerManager.setNbQuartzMined(bleachPlayerManager.getNbQuartzMined()+1);
                        ActionBar.sendMessage(player, "§c» §f§lQuartz minés:§c " + bleachPlayerManager.getNbQuartzMined() +"/"+arrancarRole.getNbQuartz() +" «");
                        if(bleachPlayerManager.getNbQuartzMined() == arrancarRole.getNbQuartz()){
                            bleachPlayerManager.setFormeLibererDurationRemining(60*9);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez récupéré assez de quartz ! Vous pouvez être sous forme libérée.");
                            main.getInventoryUtils().giveItemSafely(player, new FormLiberer(main).toItemStack());
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eVous avez déjà récupéré assez de quartz !");
                    }
                }
                if(player.getGameMode() != GameMode.CREATIVE){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCeroBlockedByJushiro(CeroExploseEvent event){
        UhcHost.debug("CeroBlockedByJushiro");
        Location location = event.getExploseLocation();
        Player player = event.nearPlayer();
        if(player != null){
            UhcHost.debug("CeroBlockedByJushiro nearPlayer");
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.isAlive()){
                if(playerManager.getRole() instanceof JushiroUkitake){
                    UhcHost.debug("JushiroUkitake");
                    if(player.isBlocking()){
                        UhcHost.debug("JushiroUkitake is blocking");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public void setNuitNoir(boolean nuitNoir) {
        this.nuitNoir = nuitNoir;
    }

    public boolean isNuitNoir() {
        return nuitNoir;
    }

    private void ceroMoyenEffectToPlayer(Player player){
        int random = UhcHost.getRANDOM().nextInt(3);
        if(random == 0){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*5, 0, false, false));
        } else if(random == 1){
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*15, 0, false, false));
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*7, 0, false, false));
        }
    }
}
