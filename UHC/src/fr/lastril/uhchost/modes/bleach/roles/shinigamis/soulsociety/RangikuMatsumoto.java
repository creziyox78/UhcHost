package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Haineko;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class RangikuMatsumoto extends Role implements ShinigamiRole, RoleListener {

    private final List<BukkitTask> particlesTasks = new ArrayList<>();
    private Cuboid area;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Haineko(main).toItemStack());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Block block = event.getBlock();
        if(block.getType() == Material.ENDER_PORTAL_FRAME){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTryNanaoInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() != null && event.getItem().getType() != Material.AIR){
            if(event.getItem().getType() == Material.IRON_SWORD ||
                    event.getItem().getType() == Material.DIAMOND_SWORD){
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
                    if(playerManager.hasRole() && playerManager.isAlive()){
                        if(playerManager.getRole() instanceof RangikuMatsumoto){
                            if(area != null){
                                if(bleachPlayerManager.canUsePower()){
                                    if(playerManager.getRoleCooldownToshiroZone() <= 0){
                                        Player target = ClassUtils.getTargetPlayer(player, 15);
                                        if(target != null){
                                            if(area.contains(target)){
                                                ClassUtils.setCorrectHealth(target, target.getHealth() - 4D, false);
                                                playerManager.setRoleCooldownToshiroZone(3);
                                            }
                                        } else {
                                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous devez visé un joueur !");
                                        }
                                    } else {
                                        player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownToshiroZone()));
                                    }
                                } else {
                                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                                }
                            }
                        }
                    }
                }
            }
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxYjI5N2E4Mjk0NmJhZGI2MDg2MjcyNjE5ZjBmMmQyNWJhZTZkMTg5MTg4ZjFiZmQzZWJkYWRmZDU2MThhNCJ9fX0=");

    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Rangiku Matsumoto";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void createZone(Location loc){
        Cuboid cuboid = new Cuboid(loc, 10);
        for (Block block : cuboid.getBlocks()){
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () ->
                    WorldUtils.spawnParticle(block.getLocation(), EnumParticle.CLOUD),1,1);
            particlesTasks.add(task);
        }
        cuboid = cuboid.expand(Cuboid.CuboidDirection.Up, 2);
        Cuboid finalCuboid = new Cuboid(cuboid).expand(Cuboid.CuboidDirection.Down, 1);
        area = finalCuboid;
        BukkitTask regenTask = Bukkit.getScheduler().runTaskTimer(main, () ->
                main.getPlayerManagerOnlines().forEach(playerManager -> {
                    Player player = playerManager.getPlayer();
                    if(player != null) {
                        if(finalCuboid.contains(player)) {
                            if(playerManager.getRole() instanceof RangikuMatsumoto){
                                if(player.hasPotionEffect(PotionEffectType.SPEED))
                                    player.removePotionEffect(PotionEffectType.SPEED);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*3, 0, false, false));

                            } else {
                                if(player.hasPotionEffect(PotionEffectType.BLINDNESS))
                                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));

                            }
                        }
                    }
                }), 40, 40);
        particlesTasks.add(regenTask);
    }

    public void deleteZone(){
        particlesTasks.forEach(BukkitTask::cancel);
        particlesTasks.clear();
        area = null;
    }

}
