package fr.lastril.uhchost.tools.API.particles;

import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Wave extends BukkitRunnable {

        private final Plugin plugin;
        private final Vector origin;
        private final List<List<Location>> shape;
        private final List<Location> locationInMovements;
        private final EnumParticle particle;
        private int time, index;

        public Wave(Plugin plugin, Vector origin, List<List<Location>> shape, int time, EnumParticle particle) {
            this.plugin = plugin;
            this.origin = origin;
            this.shape = shape;
            this.start(2);
            this.time = time;
            this.locationInMovements = new ArrayList<>();
            this.particle = particle;
        }

        /**
         * Starts The Timer
         *
         * @param delay
         */
        public void start(int delay) {
            super.runTaskTimer(this.plugin, 0, delay);
        }

        /**
         * Stops The Timer
         */
        public void stop() {
            cancel();
        }

        @Override
        public void run() {
            for (Location loc : shape.get(index)) {
                locationInMovements.add(loc);
                new BukkitRunnable(){
                    double t = 0;

                    public void run(){
                        t = t + 0.5;
                        Vector direction = loc.getDirection().normalize();
                        double x = direction.getX() * t;
                        double y = direction.getY() * t + 1.5;
                        double z = direction.getZ() * t;
                        loc.add(x,y,z);
                        ParticleEffect.playEffect(particle, loc);
                        if(particle == EnumParticle.FLAME){
                            loc.getBlock().setType(Material.FIRE);
                        }
                        loc.subtract(x,y,z);

                        if (t > time){
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
            cancel();
        }

    public List<Location> getLocationInMovements() {
        return locationInMovements;
    }
}