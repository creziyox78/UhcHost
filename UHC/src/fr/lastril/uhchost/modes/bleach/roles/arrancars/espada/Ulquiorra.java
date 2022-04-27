package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroFort;
import fr.lastril.uhchost.modes.bleach.ceros.CeroMoyen;
import fr.lastril.uhchost.modes.bleach.items.Ailes;
import fr.lastril.uhchost.modes.bleach.items.Cifer;
import fr.lastril.uhchost.modes.bleach.items.LanzaDelRelampago;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety.Omaeda;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ulquiorra extends Role implements ArrancarRole, CeroUser, RoleListener {

    private Entity leashedEntity;

    public Ulquiorra() {
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(leashedEntity != null) {
            if (player.getLocation().distanceSquared(leashedEntity.getLocation()) > 10.0D)
                leashedEntity.setVelocity(player.getLocation().toVector().subtract(leashedEntity.getLocation().toVector()).multiply(0.05D));
        }
    }

    @Override
    public int getNbQuartz() {
        return 60;
    }

    @Override
    public void onTransformationFirst() {

    }

    @Override
    public void onUnTransformationSecond() {

    }

    @Override
    public boolean canUseCero() {
        Player ulquiorra = super.getPlayer();
        if(ulquiorra != null) {
            PlayerManager playerManager = main.getPlayerManager(ulquiorra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.canUsePower() && playerManager.getRoleCooldownCeroMoyen() <= 0) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if(event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof Snowball) {
            Player player = (Player) event.getEntity().getShooter();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            if(playerManager.getRole() instanceof Ulquiorra) {
                Snowball snowball = (Snowball) event.getEntity();
                if(snowball.getCustomName() != null && snowball.getCustomName().equalsIgnoreCase("§fLanza Del Relampago")) {
                    snowball.getLocation().getWorld().createExplosion(snowball.getLocation(), 10F, false);
                }
            }
        }
    }

    @Override
    public void onUseCero() {
        Player ulquiorra = super.getPlayer();
        if(ulquiorra != null) {
            PlayerManager playerManager = main.getPlayerManager(ulquiorra.getUniqueId());
            if(playerManager.getRoleCooldownCeroMoyen() <= 0) {
                playerManager.setRoleCooldownCeroMoyen(60);
            }
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
    public AbstractCero getCero() {
        Player ulquiorra = super.getPlayer();
        if(ulquiorra != null) {
            PlayerManager playerManager = main.getPlayerManager(ulquiorra.getUniqueId());
            BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
            if(bleachPlayerManager.isInFormeLiberer()) {
                return new CeroFort();
            }
        }
        return new CeroMoyen();
    }

    @EventHandler
    public void onFish(PlayerFishEvent event){
        Entity entity = event.getCaught();
        if(entity != null){
            if(event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY){
                Player player = event.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.getRole() instanceof Ulquiorra){
                    leashedEntity = null;
                }
            }
        }

    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.getRole() instanceof Ulquiorra) {
            if(player.getItemInHand().getType() != Material.FISHING_ROD) {
                leashedEntity = null;
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
            if(playerManager.hasRole() && playerManager.getRole() instanceof Ulquiorra){
                if(bleachPlayerManager.canUsePower()){
                    leashedEntity = event.getEntity();
                } else {
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(leashedEntity != null && leashedEntity == player) {
            leashedEntity = null;
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(leashedEntity != null && leashedEntity == player) {
            leashedEntity = null;
        }
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Ailes(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new LanzaDelRelampago(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Cifer(main).toItemStack());
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
        return Camps.ARRANCARS;
    }

    @Override
    public String getRoleName() {
        return "Ulquiorra";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }
}
