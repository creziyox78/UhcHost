package fr.lastril.uhchost.modes.bleach.roles.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Gegetsuburi;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class Omaeda extends Role implements RoleListener {

    private Entity grabed;

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(13D*2D);
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Gegetsuburi(main).toItemStack());
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
        return "Omaeda";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Entity entity = event.getCaught();
        if(entity != null){
            if(event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY){
                Player player = event.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.hasRole() && playerManager.getRole() instanceof Omaeda){
                    if(playerManager.getRoleCooldownGegetsuburiGrab() <= 0){
                        ClassUtils.pullEntityToLocation(entity, player.getLocation());
                        playerManager.setRoleCooldownGegetsuburiGrab(20);
                    }
                    else {
                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownGegetsuburiGrab()));
                    }
                } else {
                    player.sendMessage(Messages.not("Omaeda"));
                }
            }
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof FishHook) {
            FishHook hook = (FishHook)event.getDamager();
            if (!(hook.getShooter() instanceof Player))
                return;
            Player player = (Player)hook.getShooter();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof Omaeda){
                Omaeda omaeda = (Omaeda) playerManager.getRole();
                omaeda.setGrabed(event.getEntity());
                if (event.getEntity() instanceof Player) {
                    Player hooked = (Player)event.getEntity();
                    hooked.sendMessage("§6Vous avez attrapé par \"§bGegetsuburi\" !");
                    player.sendMessage("§6Vous avez attrapé "+hooked.getName()+" !");
                } else {
                    String entityName = event.getEntityType().toString().replace("_", " ").toLowerCase();
                    player.sendMessage("§6Vous avez attrapé un "+entityName+" !");
                }
            }
        }
    }

    public Entity getGrabed() {
        return grabed;
    }

    public void setGrabed(Entity grabed) {
        this.grabed = grabed;
    }
}
