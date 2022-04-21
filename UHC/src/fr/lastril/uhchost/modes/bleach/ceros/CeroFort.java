package fr.lastril.uhchost.modes.bleach.ceros;

import com.avaje.ebeaninternal.server.core.Message;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.ceros.events.CeroExploseEvent;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CeroFort extends AbstractCero{

    public CeroFort() {
        super("§c§lCero Fort", CeroType.CEROS_FORT, 10, (byte) 1);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 15 blocs/s");
        addLore("§fTaille de l'explosion :§b 7x7 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b3 coeurs de dégâts");
        addLore("§b- §bEnflammé pendant 10 secondes (sans pouvoir s'éteindre)");
    }

    @Override
    public void action(QuickEvent event) {
        Player player = event.getPlayer();
        UhcHost.debug("CeroFort used by " + player.getName());
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        CeroUser ceroUser = (CeroUser) playerManager.getRole();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 2, false, false));
        List<Location> line = new ArrayList<>();

        Bukkit.getScheduler().runTaskLater(main, () -> {
            Location startLocation = player.getLocation().add(0, 1.5, 0);
            line.add(startLocation);
            Vector right = getRightHeadDirection(player).multiply(1), left = getLeftHeadDirection(player).multiply(1);
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(right)).add(0, 1, 0));
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(left)).add(0, 1, 0));
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(left)).add(0, -1, 0));
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(right)).add(0, -1, 0));
            LaserFort(line.get(0), ceroUser, true, player);
            line.remove(0);
            for (Location location : line) {
                LaserFort(location, ceroUser, false, player);
            }
        }, 20*3);

    }


    private void LaserFort(Location startLocation, CeroUser ceroUser, boolean explode, Player user) {
        Location location = startLocation.clone();
        new BukkitRunnable() {
            final double t = 0.75;
            @Override
            public void run() {
                Vector direction = location.getDirection().normalize();
                double x = direction.getX() * t;
                double y = direction.getY();
                double z = direction.getZ() * t;

                //CeroFort.super.spawnLaser(startLocation, location.add(x, y, z), 255, 0, 0);
                CeroFort.super.spawnLaser(startLocation, location.add(x, y, z), ceroUser.getCeroRedValue(), ceroUser.getCeroGreenValue(), ceroUser.getCeroBlueValue());
                if(needExplosion(startLocation, location, user)) {
                    if(explode) {
                        Bukkit.getPluginManager().callEvent(new CeroExploseEvent(location, CeroType.CEROS_FORT));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 1);
    }
}
