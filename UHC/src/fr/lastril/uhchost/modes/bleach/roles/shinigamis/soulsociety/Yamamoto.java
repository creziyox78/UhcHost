package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.commands.CmdEnnetsu;
import fr.lastril.uhchost.modes.bleach.items.RyujinJakkaItem;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.roles.*;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Yamamoto extends Role implements RoleListener, RoleCommand, ShinigamiRole {

    private int sideLength = 5;
    private int height = 3;

    private final List<Player> noAbsoplayerList = new ArrayList<>();

    private int use = 0;

    public Yamamoto(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.FIRE_ASPECT, 2).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Livre(Enchantment.ARROW_FIRE, 1).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new RyujinJakkaItem(main).toItemStack());
    }

    @EventHandler
    public void onFallOnLave(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
            Block block = player.getLocation().getBlock();
            if(playerManager.hasRole() && playerManager.isAlive()){
                if(playerManager.getRole() instanceof Yamamoto){
                    if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
                        if(block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA){
                            event.setCancelled(true);
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
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        Location entLoc = player.getLocation();
        int delta = (sideLength / 2);
        Location corner1 = new Location(entLoc.getWorld(), entLoc.getBlockX() + delta, entLoc.getBlockY() + 1, entLoc.getBlockZ() - delta);
        Location corner2 = new Location(entLoc.getWorld(), entLoc.getBlockX() - delta, entLoc.getBlockY() + 1, entLoc.getBlockZ() + delta);
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
        for(int x = minX; x <= maxX; x++) {
            for(int y = -2; y < height; y++) {
                for(int z = minZ; z <= maxZ; z++) {
                    if((x == minX || x == maxX) || (z == minZ || z == maxZ) || y == -2) {
                        Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y, z);
                        if(b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA){
                            player.sendBlockChange(b.getLocation(), Material.AIR, (byte)2);
                        }
                    }
                    if(y == height - 1) {
                        Block b = corner1.getWorld().getBlockAt(x, entLoc.getBlockY() + y + 1, z);
                        if(b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA){
                            player.sendBlockChange(b.getLocation(), Material.AIR, (byte)2);
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onEatApple(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.GOLDEN_APPLE) {
            Player player = event.getPlayer();
            if(noAbsoplayerList.contains(player)){
                Bukkit.getScheduler().runTaskLater(main, () -> {
                    if(player.hasPotionEffect(PotionEffectType.ABSORPTION)){
                        player.removePotionEffect(PotionEffectType.ABSORPTION);
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVous ne recevez pas d'Absorption car vous avez touché par le \"§6Ryujin Jakka\"§c de§9 Yamamoto§c pendant les 10 dernières secondes.");
                    }
                }, 20*2);
            }
        }
    }

    public void addBurningPlayer(Entity entity){
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () -> {
            entity.setFireTicks(20*10);
            if(entity instanceof Player){
                Player player = (Player) entity;
                PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                if(playerManager.hasRole() && !(playerManager.getRole() instanceof Yamamoto)){
                    UhcHost.debug("Added task to " + entity.getName() + " for burning");
                    noAbsoplayerList.add(player);
                }
            }
        }, 0, 20);
        UhcHost.debug("Starting task removing " + entity.getName() + " from noAbsoplayerList");
        Bukkit.getScheduler().runTaskLater(main, () -> {
            UhcHost.debug("Removing task");
            if(entity instanceof Player){
                Player player = (Player) entity;
                UhcHost.debug("Remove Yamamoto no abso list: "  + player.getName());
                noAbsoplayerList.remove(player);
            }
            task.cancel();
        }, 20*10);
    }

    public void ennetsuPower(Player player){
        List<Location> blockLocations = ClassUtils.getCircle(player.getLocation(), 7, 8);
        List<Location> lavaLocs = new ArrayList<>();
        for (Location blockLocation : blockLocations) {
            Location location = blockLocation.add(0, 5, 0);
            lavaLocs.add(location);
            location.getBlock().setType(Material.LAVA);
        }
        Bukkit.getScheduler().runTaskLater(main, () -> {
            lavaLocs.forEach(location -> location.getBlock().setType(Material.AIR));
        },20*60);
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJhNzZkZWEzZGZjMzU0NzRlMzc0Yzk3NjIxNDQ4ZDVmMTczOWYzMmY0MWYyMWRkZGY2ZmE1NTVkMGMzOWEifX19");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Yamamoto";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public List<ModeSubCommand> getSubCommands() {
        return Arrays.asList(new CmdEnnetsu(main));
    }

    public boolean hasReachedUse(){
        return use >= 2;
    }

    public void addUse(){
        use++;
    }
}
