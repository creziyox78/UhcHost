package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.soulsociety.Yamamoto;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.ParticleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class RyujinJakkaItem extends QuickItem {

    private Yamamoto yamamoto;

    public RyujinJakkaItem(UhcHost main) {
        super(Material.BLAZE_ROD);
        super.setName("§6Ryujin Jakka");
        super.setLore("",
                "§7Fait apparaître une vague§6 de feu",
                "§7sur 20 blocs. Les joueurs touchés brûlent",
                "§7et§c ne peuvent pas§e s'éteindre§7.");
        super.addEnchant(Enchantment.DURABILITY, 1, false);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole()){
                if(playerManager.getRole() instanceof Yamamoto){
                    if(playerManager.getRoleCooldownRyujinJakka() <=0){
                        this.yamamoto = (Yamamoto) playerManager.getRole();
                        Location initialLocation = player.getLocation().clone();
                        initialLocation.setPitch(0.0f);
                        //playerManager.setRoleCooldownRyujinJakka(10*60);
                        Vector direction = initialLocation.getDirection();
                        List<List<Location>> shape = new ArrayList<>();

                        List<Location> line = new ArrayList<>();

                        line.add(initialLocation.clone().add(direction));
                        for (int j = 0; j <= 4; j++) {
                            Vector right = this.getRightHeadDirection(player).multiply(j), left = this.getLeftHeadDirection(player).multiply(j);
                            line.add(initialLocation.clone().add(direction.clone().add(right)));
                            line.add(initialLocation.clone().add(direction.clone().add(left)));
                        }
                        shape.add(line);
                        Wave wave = new Wave(UhcHost.getInstance(), initialLocation.toVector(), shape);
                    } else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRyujinJakka()));
                    }

                } else {
                    player.sendMessage(Messages.not("Yamamoto"));
                }
            }
        });
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
            for (Location loc : shape.get(index)) {
                new BukkitRunnable(){
                    double t = 0;

                    public void run(){
                        t = t + 0.5;
                        Vector direction = loc.getDirection().normalize();
                        double x = direction.getX() * t;
                        double y = direction.getY() * t + 1.5;
                        double z = direction.getZ() * t;
                        loc.add(x,y,z);
                        ParticleEffect.playEffect(EnumParticle.FLAME, loc);
                        for(Entity entity : loc.getWorld().getNearbyEntities(loc, 2, 2, 2)){
                            yamamoto.addBurningPlayer(entity);
                        }
                        loc.getBlock().setType(Material.FIRE);
                        loc.subtract(x,y,z);

                        if (t > 20){
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
            cancel();
        }
    }
}
