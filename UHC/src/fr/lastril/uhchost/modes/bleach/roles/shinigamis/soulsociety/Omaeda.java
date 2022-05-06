package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Gegetsuburi;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class Omaeda extends Role implements RoleListener, ShinigamiRole {

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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJlZTlmNjg1NTlkNGJlZGNkZjFjNWExMzEwNTQ4YWRiZTRmM2U4YWYxYjNlZTk3YTg2NTEwYWM4ZGU0MjcifX19");

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
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Entity entity = event.getCaught();
        if(entity != null){
            if(event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY){
                Player player = event.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                if(playerManager.hasRole() && playerManager.getRole() instanceof Omaeda){
                    if(bleachPlayerManager.canUsePower()){
                        if(playerManager.getRoleCooldownGegetsuburiGrab() <= 0){
                            ClassUtils.pullEntityToLocation(entity, player.getLocation(), 0.07, 0.03, 0.07);
                            playerManager.setRoleCooldownGegetsuburiGrab(20);
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownGegetsuburiGrab()));
                        }
                    } else {
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                    }
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
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.hasRole() && playerManager.getRole() instanceof Omaeda){
                if(bleachPlayerManager.canUsePower()){
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
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
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
