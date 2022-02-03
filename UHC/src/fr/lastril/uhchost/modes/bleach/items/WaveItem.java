package fr.lastril.uhchost.modes.bleach.items;

import com.avaje.ebeaninternal.server.core.Message;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Hinamori;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
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

public class WaveItem extends QuickItem {

    private Hinamori hinamori;

    public WaveItem(UhcHost main) {
        super(Material.BLAZE_POWDER);
        super.setName("§6Wave");
        super.addEnchant(Enchantment.DURABILITY, 1, true);
        super.addItemFlag(ItemFlag.HIDE_ENCHANTS);
        super.setLore("",
                "§7Envoie une vague de§6 flame§7 (10x2).",
                "§7Les joueurs touchés sont",
                "§7attirés vers l'utilisateur",
                "§7et§c ne peuvent pas s'éteindre",
                "§7pendant 20 secondes.",
                "§7(Cooldown - 10 minutes)");
        super.onClick(onClick -> {
            Player player = onClick.getPlayer();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Hinamori){
                hinamori = (Hinamori) playerManager.getRole();
                if(playerManager.getRoleCooldownWave() <= 0){
                    Location initialLocation = player.getLocation().clone();
                    initialLocation.setPitch(0.0f);
                    playerManager.setRoleCooldownWave(10*60);
                    Vector direction = initialLocation.getDirection();
                    List<List<Location>> shape = new ArrayList<>();

                    List<Location> line = new ArrayList<>();

                    line.add(initialLocation.clone().add(direction));
                    for (int j = 0; j <= 4; j++) {
                        Vector right = this.getRightHeadDirection(player).multiply(j), left = this.getLeftHeadDirection(player).multiply(j);
                        line.add(initialLocation.clone().add(direction.clone().add(right)));
                        line.add(initialLocation.clone().add(direction.clone().add(left)));
                        line.add(initialLocation.clone().add(direction.clone().add(left)).add(0, 1, 0));
                        line.add(initialLocation.clone().add(direction.clone().add(right)).add(0, 1, 0));
                        line.add(initialLocation.clone().add(direction.clone().add(left)).add(0, 2, 0));
                        line.add(initialLocation.clone().add(direction.clone().add(right)).add(0, 2, 0));
                    }
                    shape.add(line);
                    Wave wave = new Wave(UhcHost.getInstance(), initialLocation.toVector(), shape);
                } else {
                    player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownWave()));
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
                            hinamori.addBurningPlayer(entity, 20);
                            if(hinamori.getPlayer() != null && entity != hinamori.getPlayer()){
                                ClassUtils.pullEntityToLocation(entity, hinamori.getPlayer().getLocation());
                                entity.sendMessage("§6Vous avez été touché par la \"Wave\" d'Hinamori. Vous ne pouvez pas vous éteindre pendant 20 secondes.");
                            }
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
