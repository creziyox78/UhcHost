package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.modes.bleach.items.Snap;
import fr.lastril.uhchost.modes.bleach.items.WaveItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitTask;

public class Hinamori extends Role implements RoleListener {

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new WaveItem(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Snap(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @EventHandler
    public void onShotFireBall(EntityExplodeEvent event){
        if(event.getEntity() instanceof Fireball){
            Fireball fireball = (Fireball) event.getEntity();
            if(fireball.getCustomName().equalsIgnoreCase("§6Jakuho Raikoben")){
                for(Entity entity : fireball.getNearbyEntities(3, 3, 3)){
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        addBurningPlayer(player, 15);
                    }
                }
            }
        }
    }

    @Override
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Hinamori";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    public void addBurningPlayer(Entity entity, int fireSeconds){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () -> entity.setFireTicks(20*fireSeconds), 0, 20);
        Bukkit.getScheduler().runTaskLater(main, task::cancel, 20L * fireSeconds);
    }
}
