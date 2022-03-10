package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.lg.roles.LGHideDeath;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ermite extends Role implements LGRole, LGHideDeath {

    private final int distance = 30;
    private boolean day;

    @Override
    public void giveItems(Player player) {
    }

    @Override
    public void onNight(Player player) {
        day = false;
    }

    @Override
    public void onDay(Player player) {
        day = true;
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Ermite";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        int nearLgPlayer = 0;
        if(playerManager.hasRole() && playerManager.isAlive()){
            for(Entity entity : player.getNearbyEntities(distance, distance, distance)){
                if(entity instanceof Player){
                    Player nearPlayer = (Player) entity;
                    if(nearPlayer != player){
                        PlayerManager nearPlayerManager = main.getPlayerManager(nearPlayer.getUniqueId());
                        if(nearPlayerManager.isAlive() && nearPlayerManager.hasRole()){
                            nearLgPlayer++;
                        }
                    }
                }
            }
            giveManagerEffect(player, nearLgPlayer);
        }
    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

    private void giveManagerEffect(Player player, int nearPlayer){
         if(nearPlayer <= 3){
             if(player.hasPotionEffect(PotionEffectType.SPEED))
                 player.removePotionEffect(PotionEffectType.SPEED);
             if(nearPlayer == 0){
                 player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*4, 0, false, false));
             }
             if(day){
                 if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                     player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                 player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false, false));
             } else {
                 if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                     player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                 player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 0, false, false));
             }
        } else {
            if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*4, 0, false, false));
        }
    }

}
