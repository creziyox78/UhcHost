package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Brazo;
import fr.lastril.uhchost.modes.bleach.items.Muerte;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Sado extends Role implements ShinigamiRole, RoleListener {

    private ARMS_FORM arms_form = ARMS_FORM.ARM_GEANT;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Brazo(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Muerte(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(arms_form == ARMS_FORM.ARM_GEANT) {
            if(player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*4, 0, false , false));
            bleachPlayerManager.setSpeedPourcentage(0);
        }
        if(arms_form == ARMS_FORM.ARM_DIABLE) {
            if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*4, 0, false , false));
            bleachPlayerManager.setSpeedPourcentage(10);
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                .setName(getCamp().getCompoColor() + getRoleName())
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZjOTUxYjYxOTFiZDg5NmNkYTgzY2Y3MmEzNzdmNjI3ZTMxMDc4ODQ4ZWQzYzA0ZmE3OGI5MDUyN2NlYWM4OCJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Sado";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public enum ARMS_FORM{
        ARM_DIABLE("Bras du diable"),
        ARM_GEANT("Bras du géant"),
        ;
        private final String name;

        ARMS_FORM(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }

    public ARMS_FORM getForm() {
        return arms_form;
    }

    public void setForm(ARMS_FORM arms_form) {
        this.arms_form = arms_form;
    }

    @EventHandler
    public void onDamageWithArmInForm(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(damagerManager.hasRole() && damagerManager.getRole() instanceof Sado){
                Sado sado = (Sado) damagerManager.getRole();
                if(sado.getForm() == ARMS_FORM.ARM_GEANT){
                    if(damager.getItemInHand() != null && isItemInHandIsSword(damager)){
                        if(damagerManager.getRoleCooldownCriticalSado() <= 0){
                            if(damager.getHealth() <= 2D*3D){
                                ClassUtils.ripulseSpecificEntityFromLocation(player, damager.getLocation(), 2, 2);
                                event.setDamage(event.getDamage()*2);
                                damagerManager.setRoleCooldownCriticalSado(60*5);
                                damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§7Vous venez d'infligé un coup fatal à §e" + player.getName() + "§7 !");
                                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§9Sado§7 vient de vous infligé un coup fatal !");
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isItemInHandIsSword(Player player){
        return player.getItemInHand() != null && (player.getItemInHand().getType() == Material.DIAMOND_SWORD ||
                player.getItemInHand().getType() == Material.GOLD_SWORD || player.getItemInHand().getType() == Material.IRON_SWORD ||
                player.getItemInHand().getType() == Material.STONE_SWORD || player.getItemInHand().getType() == Material.WOOD_SWORD);
    }
}
