package fr.lastril.uhchost.modes.bleach.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Hinamori;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.Wave;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class WaveItem extends QuickItem {
    public WaveItem(UhcHost main) {
        super(Material.BLAZE_POWDER);
        super.setName("§6Wave");
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
                Hinamori hinamori = (Hinamori) playerManager.getRole();
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
                    Wave wave = new Wave(UhcHost.getInstance(), initialLocation.toVector(), shape, 20, EnumParticle.FLAME);
                    wave.getLocationInMovements().forEach(location -> {
                        for(Entity entity : location.getWorld().getNearbyEntities(location, 0.5, 0.5, 0.5)){
                            hinamori.addBurningPlayer(entity, 20);
                            ClassUtils.pullEntityToLocation(entity, player.getLocation());
                        }
                    });
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
