package fr.lastril.uhchost.modes.ds.roles.demon;

import fr.lastril.uhchost.modes.bleach.items.Oeil;
import fr.lastril.uhchost.modes.ds.speciality.train.TrainLocation;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Enmu extends Role {

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Oeil(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(isInTrain(player)){
            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false, false));
            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 0, false, false));
        } else {
            if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*3, 0, false, false));
        }
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
        return Camps.DEMONS;
    }

    @Override
    public String getRoleName() {
        return "Enmu";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "ds.yml");
    }

    public boolean isInTrain(Player player){
        if(player.getWorld() == Bukkit.getWorld("world_the_end")){
            for(TrainLocation trainLocation: TrainLocation.values()){
                if(trainLocation.getWaggonCuboid().contains(player))
                    return true;
            }
        }
        return false;
    }
}
