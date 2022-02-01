package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Yamamoto;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.ParticleEffect;
import fr.lastril.uhchost.tools.API.particles.Wave;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
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
                        playerManager.setRoleCooldownRyujinJakka(10*60);
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
                        Wave wave = new Wave(UhcHost.getInstance(), initialLocation.toVector(), shape, 20, EnumParticle.FLAME);
                        wave.getLocationInMovements().forEach(location -> {
                            for(Entity entity : location.getWorld().getNearbyEntities(location, 2, 2, 2)){
                                yamamoto.addBurningPlayer(entity);
                            }
                        });
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
}
