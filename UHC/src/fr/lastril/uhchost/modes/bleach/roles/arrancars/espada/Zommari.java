package fr.lastril.uhchost.modes.bleach.roles.arrancars.espada;

import com.avaje.ebeaninternal.api.ClassUtil;
import fr.lastril.uhchost.modes.bleach.ceros.AbstractCero;
import fr.lastril.uhchost.modes.bleach.ceros.CeroType;
import fr.lastril.uhchost.modes.bleach.items.Effet;
import fr.lastril.uhchost.modes.bleach.roles.ArrancarRole;
import fr.lastril.uhchost.modes.bleach.roles.CeroUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Zommari extends Role implements ArrancarRole, RoleListener {

    private boolean preventDead = false;
    private PotionEffect copiedEffect;

    private final List<Location> cages = new ArrayList<>();
    @Override
    public int getNbQuartz() {
        return 70;
    }

    @Override
    public void onTransformationFirst() {

    }

    @Override
    public void onUnTransformationFirst() {

    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Effet(main).toItemStack());
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
        return "Zommari";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(playerManager.getRole() instanceof Zommari){
            if(!hasPreventDead()){
                playerManager.setDeathLocation(player.getLocation());
            }
        }
    }


    @EventHandler
    public void onDestroyBlockCage(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.HUGE_MUSHROOM_1) {
            if(cages.contains(event.getBlock().getLocation())){
                Player player = event.getPlayer();
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(!(playerManager.getRole() instanceof Zommari) && player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                }
            }
        }
    }


    public PotionEffect getCopiedEffect() {
        return copiedEffect;
    }

    public void setCopiedEffect(PotionEffect copiedEffect) {
        this.copiedEffect = new PotionEffect(copiedEffect.getType(), 20*5*60, copiedEffect.getAmplifier(), false, false);
    }

    public boolean hasPreventDead() {
        return preventDead;
    }

    public void respawn(Player player) {
        preventDead = false;
        player.spigot().respawn();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        ClassUtils.ripulseEntityFromLocation(playerManager.getDeathLocation(), 5, 2, 1);
        Bukkit.getScheduler().runTaskLater(main, () -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(2D*4D);
        }, 20);
        player.teleport(playerManager.getDeathLocation());
        ClassUtils.getSphere(player.getLocation(), 4, 4, 4, false).forEach(location -> {
            Block block = location.getBlock();
            if(block.getType() != null && block.getType() != Material.AIR) {
                block.setType(Material.HUGE_MUSHROOM_1);
                cages.add(location);
            }
        });
        Bukkit.getScheduler().runTaskLater(main, cages::clear, 20*15);
    }
}
