package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.DanseDuVent;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JushiroUkitake extends Role implements ShinigamiRole, RoleListener {

    private boolean isBlocking, inDanseDuVent;

    public JushiroUkitake(){
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof JushiroUkitake){
            if(isBlocking && !player.isBlocking()){
                isBlocking = false;
                playerManager.setRoleCooldownRenvoie(60);
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new DanseDuVent(main).toItemStack());
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
        return "Jushiro Ukitake";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onProtectWithSword(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());

            if(playerManager.hasRole() && playerManager.getRole() instanceof JushiroUkitake){
                /*
                 * Blocking with sword effect
                 */
                if(player.isBlocking() && !isBlocking){
                    if(event.getDamager() instanceof Projectile){
                        if(playerManager.getRoleCooldownRenvoie() <= 0){
                            isBlocking = true;
                            event.setCancelled(true);
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownRenvoie()));
                        }
                    }
                }
            }
            if(event.getDamager() instanceof Player){
                Player damager = (Player) event.getDamager();
                PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
                if(damagerManager.hasRole() && damagerManager.getRole() instanceof JushiroUkitake){
                    JushiroUkitake jushiroUkitake = (JushiroUkitake) damagerManager.getRole();
                    if(jushiroUkitake.inDanseDuVent){
                        for(Entity entity : player.getNearbyEntities(5 , 5 , 5)){
                            if(entity instanceof Player){
                                Player nearPlayer = (Player) entity;
                                if(nearPlayer.getUniqueId() != damager.getUniqueId()){
                                    nearPlayer.damage(event.getFinalDamage());
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public void setInDanseDuVent(boolean inDanseDuVent) {
        this.inDanseDuVent = inDanseDuVent;
    }
}
