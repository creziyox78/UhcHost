package fr.lastril.uhchost.modes.bleach.ceros;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.ceros.events.CeroExploseEvent;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CeroMoyen extends AbstractCero{

    public CeroMoyen() {
        super("§a§lCero Moyen", CeroType.CEROS_MOYEN, 5, (byte) 10);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 10 blocs/s");
        addLore("§fTaille de l'explosion :§b 4x4 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b2 coeurs de dégâts");
        addLore("§e1 effet parmis les 3 suivants :");
        addLore("§b- §7Slowness pendant 7 secondes");
        addLore("§b- §fWeakness pendant 15 secondes");
        addLore("§b- §8Blindness pendant 5 secondes");
    }

    @Override
    public void action(QuickEvent event) {
        Player player = event.getPlayer();
        UhcHost.debug("CeroMoyen used by " + player.getName());
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        CeroUser ceroUser = (CeroUser) playerManager.getRole();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 2, false, false));
        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§a§lCero Moyen §f: Chargement du tir dans 2 secondes...");
        List<Location> line = new ArrayList<>();

        Bukkit.getScheduler().runTaskLater(main, () -> {
            Location startLocation = player.getLocation().add(0, 1.5, 0);
            line.add(startLocation);
            Vector right = getRightHeadDirection(player).multiply(0.5), left = this.getLeftHeadDirection(player).multiply(0.5);
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(right)).add(0, -1, 0));
            line.add(startLocation.clone().add(startLocation.getDirection().clone().add(left)).add(0, -1, 0));
            LaserMoyen(line.get(0), ceroUser, true, player);
            line.remove(0);
            for (Location location : line) {
                LaserMoyen(location, ceroUser, false, player);
            }
        }, 20*2);
    }

    private void LaserMoyen(Location startLocation, CeroUser ceroUser, boolean explode, Player user) {
        Location location = startLocation.clone();
        new BukkitRunnable() {
            final double t = 0.5;
            @Override
            public void run() {
                Vector direction = location.getDirection().normalize();
                double x = direction.getX() * t;
                double y = direction.getY();
                double z = direction.getZ() * t;
                //CeroMoyen.super.spawnLaser(startLocation, location.add(x, y, z), -255, 255, 0);
                CeroMoyen.super.spawnLaser(startLocation, location.add(x, y, z), ceroUser.getCeroRedValue(), ceroUser.getCeroGreenValue(), ceroUser.getCeroBlueValue());
                if(needExplosion(startLocation, location, user)) {
                    if(explode) {
                        Bukkit.getPluginManager().callEvent(new CeroExploseEvent(location, CeroType.CEROS_MOYEN));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(main, 0, 1);
    }
}
