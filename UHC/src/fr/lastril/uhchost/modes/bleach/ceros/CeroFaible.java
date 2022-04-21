package fr.lastril.uhchost.modes.bleach.ceros;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.ceros.events.CeroExploseEvent;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CeroFaible extends AbstractCero{

    public CeroFaible() {
        super("§9§lCero Faible", CeroType.CEROS_MOYEN, 5, (byte) 6);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 20 blocs/s");
        addLore("§fTaille de l'explosion :§b 2x2 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b1 coeur de dégâts");
    }

    @Override
    public void action(QuickEvent event) {
        Player player = event.getPlayer();
        UhcHost.debug("CeroMoyen used by " + player.getName());
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        CeroUser ceroUser = (CeroUser) playerManager.getRole();
        Location startLocation = player.getLocation().add(0, 1.5, 0);
        LaserFaible(startLocation, ceroUser, player);
    }

    private void LaserFaible(Location startLocation, CeroUser ceroUser, Player user) {
        Location location = startLocation.clone();
        new BukkitRunnable() {
            final double t = 1;
            @Override
            public void run() {
                Vector direction = location.getDirection().normalize();
                double x = direction.getX() * t;
                double y = direction.getY();
                double z = direction.getZ() * t;
                //CeroFaible.super.spawnLaser(startLocation, location.add(x, y, z), 0, 0, 255);
                 CeroFaible.super.spawnLaser(startLocation, location.add(x, y, z), ceroUser.getCeroRedValue(), ceroUser.getCeroGreenValue(), ceroUser.getCeroBlueValue());
                if(needExplosion(startLocation, location, user)) {
                    Bukkit.getPluginManager().callEvent(new CeroExploseEvent(location, CeroType.CEROS_FAIBLE));
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 1);
    }
}
