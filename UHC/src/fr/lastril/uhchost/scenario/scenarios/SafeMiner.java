package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.inventory.guis.scenarios.SafeMinerGui;
import fr.lastril.uhchost.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;

public class SafeMiner extends Scenario {
    private static int couche = 30;

    public SafeMiner() {
        super("§eSafe Miner",
                Arrays.asList("§fRéduit les dégâts des monstres par 2", "§fet annule les dégâts de feu", "§fen dessous de la couche configuré."),
                Material.LAVA_BUCKET, SafeMinerGui.class);
    }

    @EventHandler
    public void onDamageByMob(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && !(event.getDamager() instanceof Player)){
            Player player = (Player) event.getEntity();
            if(player.getLocation().getY() <= couche){
                event.setDamage(event.getDamage() / 2);
            }
        }
    }

    @EventHandler
    public void onDamageByMob(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(player.getLocation().getY() <= couche){
                if(event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE){
                    event.setCancelled(true);
                }
            }
        }
    }

    public static int getCouche() {
        return couche;
    }

    public static void setCouche(int couche) {
        SafeMiner.couche = couche;
    }
}
