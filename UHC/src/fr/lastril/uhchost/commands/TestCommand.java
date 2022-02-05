package fr.lastril.uhchost.commands;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.tools.API.Cuboid;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TestCommand implements CommandExecutor {

    private final UhcHost main = UhcHost.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            /*if(Bukkit.getWorlds().contains(Bukkit.getWorld("soulsociety"))){
                player.teleport(new Location(Bukkit.getWorld("soulsociety"), 0,80, 0));
                player.sendMessage("§7Téléportation...");
            } else {
                player.sendMessage("§eLe monde n'existe pas, chargement...");
                WorldCreator.name("soulsociety").createWorld();
                player.sendMessage("§aChargement terminé !");
            }

            Wave wave = new Wave(main, player.getLocation());
            new BukkitRunnable(){

                @Override
                public void run() {
                    wave.stop();
                }
            }.runTaskLater(main, 20*10);*/
            player.sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§a§lFin du test.");
        }
        return false;
    }


    public class Wave extends BukkitRunnable {

        private final UhcHost plugin;
        private final Location l;
        private int rad = 1;
        private int id;

        public Wave(UhcHost plugin, Location l) {
            this.plugin = plugin;
            this.l = l;
            start(2);
        }

        /**
         * Return A List Of Locations That
         * Make Up A Circle Using A Provided
         * Center, Radius, And Desired Points.
         *
         * @param center
         * @param radius
         * @param amount
         * @return Location
         */
        private List<Location> getCircle(Location center, double radius, int amount) {
            World world = center.getWorld();
            double increment = ((2 * Math.PI) / amount);
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                double angle = i * increment;
                double x = center.getX() + (radius * Math.cos(angle));
                double z = center.getZ() + (radius * Math.sin(angle));
                locations.add(new Location(world, x, center.getY(), z));
            }
            return locations;
        }

        /**
         * Starts The Timer
         *
         * @param delay
         */
        private void start(int delay) {
            super.runTaskTimer(plugin, 0, delay);
        }

        /**
         * Stops The Timer
         */
        protected void stop() {
            cancel();
        }

        @Override
        public void run() {
            for (Location loc : getCircle(l, rad, (rad * ((int) (Math.PI * 2))))) {
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, loc.getBlock().getType(), loc.getBlock().getData());
                fb.setVelocity(new Vector(0,0.3,0));
                loc.getBlock().setType(Material.AIR);
            }
            rad++;
            rad = (((rad % 20) == 0) ? 1 : rad);
        }
    }

}
