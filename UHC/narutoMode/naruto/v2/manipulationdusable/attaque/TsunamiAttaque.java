package fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque;

import fr.atlantis.api.utils.WorldUtils;
import fr.maygo.uhc.modes.naruto.v2.manipulationdusable.SandShape;
import fr.maygo.uhc.modes.naruto.v2.manipulationdusable.ShapeType;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TsunamiAttaque extends SandShape {

    public TsunamiAttaque() {
        super("Tsunami de sable", ShapeType.ATTAQUE);
    }

    @Override
    public boolean useCapacity(Player player, Gaara gaara) {
        Location initialLocation = player.getLocation().clone();
        initialLocation.setPitch(0.0f);

        Vector direction = initialLocation.getDirection();

        List<List<Location>> shape = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            List<Location> line = new ArrayList<>();

            Vector front = direction.clone().multiply(i);

            line.add(initialLocation.clone().add(front));
            for (int j = 0; j <= 2; j++) {
                Vector right = this.getRightHeadDirection(player).multiply(j), left = this.getLeftHeadDirection(player).multiply(j);


                line.add(initialLocation.clone().add(front.clone().add(right)));
                line.add(initialLocation.clone().add(front.clone().add(left)));
            }
            shape.add(line);
        }


        Wave wave = new Wave(main, initialLocation.toVector(), shape);

        return true;
    }

    @Override
    public int getSandPrice() {
        return 30;
    }

    private Vector getRightHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
    }

    private Vector getLeftHeadDirection(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
    }

    public class Wave extends BukkitRunnable {

        private final Plugin plugin;
        private final Vector origin;
        private final List<List<Location>> shape;
        private int index;

        public Wave(Plugin plugin, Vector origin, List<List<Location>> shape) {
            this.plugin = plugin;
            this.origin = origin;
            this.shape = shape;
            this.start(2);
        }

        /**
         * Starts The Timer
         *
         * @param delay
         */
        private void start(int delay) {
            super.runTaskTimer(this.plugin, 0, delay);
        }

        /**
         * Stops The Timer
         */
        protected void stop() {
            cancel();
        }

        @Override
        public void run() {
            if (index >= shape.size()) {
                cancel();
                return;
            }
            for (Location loc : shape.get(index)) {
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, Material.SAND, (byte) 0);
                fb.setDropItem(false);
                fb.setHurtEntities(true);
                fb.setVelocity(new Vector(0, .3, 0));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (WorldUtils.getDistanceBetweenTwoLocations(players.getLocation(), loc) <= 1) {
                        Vector fromPlayerToTarget = players.getLocation().toVector().clone().subtract(origin);
                        fromPlayerToTarget.multiply(4); //6
                        fromPlayerToTarget.setY(1); // 2
                        players.setVelocity(fromPlayerToTarget);
                        players.damage(2D * 2D);
                    }
                }
            }
            index++;
        }
    }
}
