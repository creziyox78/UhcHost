package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.modes.bleach.items.SodeNoShirayuki;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Byakuya;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Locale;

public class Rukia extends Role implements ShinigamiRole {

    private boolean inSode, superSode, usedSuperMode;
    private int ticks = 7, duration = 30;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new SodeNoShirayuki(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.hasRole() && playerManager.getRole() instanceof Rukia) {
            Rukia rukia = (Rukia) playerManager.getRole();
            if(rukia.isInSode()) {
                if(rukia.isSuperSode()) {
                    if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*2, 0, false, false));
                }
                if(player.hasPotionEffect(PotionEffectType.SPEED))
                    player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*2, 0, false, false));
                for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
                    if(entity instanceof Player) {
                        Player target = (Player) entity;
                        PlayerManager targetManager = main.getPlayerManager(target.getUniqueId());
                        if(!isImunise(targetManager)) {
                            if(target.hasPotionEffect(PotionEffectType.WEAKNESS))
                                target.removePotionEffect(PotionEffectType.WEAKNESS);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 2, 1, false, false));
                            if(target.hasPotionEffect(PotionEffectType.SLOW))
                                target.removePotionEffect(PotionEffectType.SLOW);
                            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 1, false, false));
                            ticks--;
                            if(ticks == 0) {
                                ticks = 7;
                                duration--;
                                if(rukia.isSuperSode()) {
                                    target.damage(2D);
                                }
                                if(duration == 0) {
                                    rukia.setSuperSode(false);
                                    rukia.setInSode(false);
                                }
                            }
                        }
                    }
                }
                Location playerLocation = player.getLocation();
                playerLocation.setY(playerLocation.getY() + 2);
                List<Location> locationList = ClassUtils.getCircle(playerLocation, 5, 20);
                for(Location location : locationList) {
                    WorldUtils.spawnParticle(location, Effect.SNOWBALL_BREAK);
                }
            }
        }
    }

    private boolean isImunise(PlayerManager playerManager) {
        if(playerManager.hasRole() && playerManager.getRole() instanceof Rukia ||
                        playerManager.hasRole() && playerManager.getRole() instanceof Byakuya ||
                                playerManager.hasRole() && playerManager.getRole() instanceof IchigoKurosaki) {
            return true;
        }
        return false;
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
        return "Rukia";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public boolean isInSode() {
        return inSode;
    }

    public void setInSode(boolean inSode) {
        this.inSode = inSode;
    }

    public boolean isSuperSode() {
        return superSode;
    }

    public void setSuperSode(boolean superSode) {
        this.superSode = superSode;
    }

    public boolean isUsedSuperMode() {
        return usedSuperMode;
    }

    public void setUsedSuperMode(boolean usedSuperMode) {
        this.usedSuperMode = usedSuperMode;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
