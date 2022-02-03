package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.items.JakuhoRaikoben;
import fr.lastril.uhchost.modes.bleach.items.Suzumebachi;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SoiFon extends Role implements RoleListener, ShinigamiRole {

    private List<PlayerManager> marquedPlayers, repulsedPlayers;
    private boolean inMarque;
    private int marquePhase = 1;

    public SoiFon(){
        this.marquedPlayers = new ArrayList<>();
        this.repulsedPlayers = new ArrayList<>();
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        for(Entity entity : player.getNearbyEntities(15, 15, 15)){
            if(entity instanceof Player){
                Player nearPlayer = (Player) entity;
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.isAlive()){
                    Location location = player.getLocation();
                    Location nearLoc = nearPlayer.getLocation();
                    if(location.distance(nearLoc) <= 15 && location.distance(nearLoc) >= 10){
                        if(nearPlayer.getHealth() <= 2D*6D){
                            if(player.hasPotionEffect(PotionEffectType.SPEED)){
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 1, false, false));
                        }
                    } else if(location.distance(nearLoc) < 10){
                        if(nearPlayer.getHealth() <= 2D*3D){
                            if(player.hasPotionEffect(PotionEffectType.SPEED)){
                                player.removePotionEffect(PotionEffectType.SPEED);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 2, false, false));
                        }
                    }
                }
            }
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Suzumebachi(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new JakuhoRaikoben(main).toItemStack());
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJiMWEwN2JiMWM4YTY3NDI4YTcyNWVjMWYyOWQ3NmM4YzFlNjM1NmQyOTVmYjQwYjBjY2Y2MjE5YTRkYjk3NSJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Soi Fon";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onFallWitherSkullDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                if(repulsedPlayers.contains(playerManager)){
                    event.setCancelled(true);
                    repulsedPlayers.remove(playerManager);
                }
            }
        }
    }

    @EventHandler
    public void onShotWitherHead(EntityExplodeEvent event){
        if(event.getEntity() instanceof WitherSkull){
            WitherSkull witherSkull = (WitherSkull) event.getEntity();
            if(witherSkull.getCustomName().equalsIgnoreCase("§6Jakuho Raikoben")){
                
                for(Entity entity : witherSkull.getNearbyEntities(10, 10, 10)){
                    if(entity instanceof Player){
                        Player player = (Player) entity;
                        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                        if(player.getHealth() - 10D <= 0.2){
                            player.setHealth(0.5);
                        } else {
                            player.setHealth(player.getHealth() - 10D);
                        }
                        repulsedPlayers.add(playerManager);
                        //ClassUtils.setCorrectHealth(player, player.getHealth() - 10, false);
                    }
                }
                ClassUtils.ripulseEntityFromLocation(witherSkull.getLocation(), 10,5, 3);
                witherSkull.getLocation().getWorld()
                        .createExplosion(witherSkull.getLocation().getX(), witherSkull.getLocation().getY(), witherSkull.getLocation().getZ(),
                                2f, false, false);


            }
        }
    }

    public boolean isInMarque() {
        return inMarque;
    }

    public void setInMarque(boolean inMarque) {
        this.inMarque = inMarque;
    }

    public void setMarquePhase(int marquePhase) {
        this.marquePhase = marquePhase;
    }

    public int getMarquePhase() {
        return marquePhase;
    }

    public void addPlayerMarqued(PlayerManager playerManager){
        marquedPlayers.add(playerManager);
    }

    public void removePlayerMarqued(PlayerManager playerManager){
        marquedPlayers.remove(playerManager);
    }

    public boolean isMarqued(PlayerManager playerManager){
        return marquedPlayers.contains(playerManager);
    }
}
