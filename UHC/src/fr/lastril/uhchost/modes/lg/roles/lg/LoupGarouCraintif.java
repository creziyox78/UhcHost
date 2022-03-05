package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGChatRole;
import fr.lastril.uhchost.modes.lg.roles.LGHideDeath;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.RealLG;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoupGarouCraintif extends Role implements LGRole, RealLG, LGChatRole, LGHideDeath {

    private int distance = 30;
    private boolean day;

    public LoupGarouCraintif() {
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 1, 0, false, false), When.AT_KILL);
        super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * 1, 0, false, false), When.AT_KILL);
    }

    @Override
    public String getRoleName() {
        return "Loup-Garou Craintif";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "lg.yml");
    }

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
    public void afterRoles(Player player) {
        player.sendMessage(sendList());
    }

    @Override
    public String sendList() {
        if (main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager) {
            LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
            return loupGarouManager.sendLGList();
        }
        return null;
    }

    @Override
    public void onNewDay(Player player) {
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
                            if(nearPlayerManager.getRole() instanceof RealLG || nearPlayerManager.getWolfPlayerManager().isTransformed() ||nearPlayerManager.getWolfPlayerManager().isInfected()){
                                nearLgPlayer++;
                            }
                        }
                    }
                }
            }
            giveManagerEffect(player, nearLgPlayer);
        }

    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===");
    }

    @Override
    public Camps getCamp() {
        return Camps.LOUP_GAROU;
    }

    private void giveManagerEffect(Player player, int nearLgPlayer){
        if(nearLgPlayer == 0){
            if(player.hasPotionEffect(PotionEffectType.SPEED))
                player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*4, 0, false, false));
            if(day){
                if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 0, false, false));
            } else {
                if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false, false));
            }
        } else if (nearLgPlayer == 1){
            if(player.hasPotionEffect(PotionEffectType.SPEED))
                player.removePotionEffect(PotionEffectType.SPEED);
            if(day){
                if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 0, false, false));
            } else {
                if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false, false));
            }
        } else if(nearLgPlayer >= 2){
            if(player.hasPotionEffect(PotionEffectType.SPEED))
                player.removePotionEffect(PotionEffectType.SPEED);
            if(day){
                if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            } else {
                if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            }
            if(nearLgPlayer >= 3){
                if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*4, 0, false, false));
            }
        }
    }

    @Override
    public boolean canSee() {
        return true;
    }

    @Override
    public boolean canSend() {
        return true;
    }

    @Override
    public boolean sendPlayerName() {
        return false;
    }
}
