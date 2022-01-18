package fr.lastril.uhchost.modes.bleach.roles.soulsociety;

import fr.lastril.uhchost.modes.bleach.items.Suzumebachi;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class SoiFon extends Role implements RoleListener, ShinigamiRole {

    private List<PlayerManager> marquedPlayers;
    private boolean inMarque;
    private int marquePhase = 1;

    public SoiFon(){
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
                    } else {
                        if(player.hasPotionEffect(PotionEffectType.SPEED)){
                            player.removePotionEffect(PotionEffectType.SPEED);
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 0, false, false));
                    }
                }
            }
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Suzumebachi(main).toItemStack());
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
    public void onDamage(Player damager, Player target) {
        PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
        if(damagerManager.hasRole()){
            if(damagerManager.getRole() instanceof SoiFon){

            }
        }
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
        return main.getRoleDescription(this, this.getClass().getName());
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
