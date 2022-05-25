package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroMoyen;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.items.Laceration;
import fr.lastril.uhchost.modes.bleach.items.Rugissement;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Grimmjow extends Role implements ArrancarRole, CeroUser {

    private Player stunned;

    public Grimmjow(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public int getNbQuartz() {
        return 70;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(stunned != null && stunned == player) {
                Player grimmjow = super.getPlayer();
                if(grimmjow != null) {
                    if(grimmjow.getHealth() + 2D > grimmjow.getMaxHealth())
                        grimmjow.setHealth(grimmjow.getMaxHealth());
                    else
                        grimmjow.setHealth(grimmjow.getHealth() + 2D);
                }
            }
        }
    }

    @Override
    public void onTransformationFirst() {
        Player grimmjow = super.getPlayer();
        if(grimmjow != null) {
            super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            if(grimmjow.hasPotionEffect(PotionEffectType.SPEED))
                grimmjow.removePotionEffect(PotionEffectType.SPEED);
            grimmjow.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @Override
    public void onUnTransformationFirst() {
        Player grimmjow = super.getPlayer();
        if(grimmjow != null) {
            super.removeEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            if(grimmjow.hasPotionEffect(PotionEffectType.SPEED))
                grimmjow.removePotionEffect(PotionEffectType.SPEED);
        }
    }

    @Override
    public boolean canUseCero(CeroType ceroType) {
        Player grimmjow = super.getPlayer();
        if(grimmjow != null) {
            PlayerManager playerManager = main.getPlayerManager(grimmjow.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(playerManager.getRoleCooldownCeroMoyen() <= 0 && bleachPlayerManager.canUsePower()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUseCero(CeroType ceroType) {
        Player grimmjow = super.getPlayer();
        if(grimmjow != null) {
            PlayerManager playerManager = main.getPlayerManager(grimmjow.getUniqueId());
            playerManager.setRoleCooldownCeroMoyen(60);
        }
    }

    @Override
    public int getCeroRedValue() {
        return 0;
    }

    @Override
    public int getCeroGreenValue() {
        return 0;
    }

    @Override
    public int getCeroBlueValue() {
        return 0;
    }

    @Override
    public List<AbstractCero> getCero() {
        return Arrays.asList(new CeroMoyen());
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Rugissement(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Laceration(main).toItemStack());
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===");
    }

    @Override
    public Camps getCamp() {
        return Camps.ARRANCARS;
    }

    @Override
    public String getRoleName() {
        return "Grimmjow";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void setStunned(Player stunned) {
        this.stunned = stunned;
    }

    public Player getStunned() {
        return stunned;
    }
}
