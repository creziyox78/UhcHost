package fr.lastril.uhchost.modes.bleach.roles.shinigamis;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Mur;
import fr.lastril.uhchost.modes.bleach.items.Rika;
import fr.lastril.uhchost.modes.bleach.items.ShunShun;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.normal.BreakBlock;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InoueOrihime extends Role implements ShinigamiRole, RoleListener {

    private boolean secondUse = false, areaCreated = false;
    private Cuboid area;
    private Cuboid wall;
    private int ticks = 7;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new ShunShun(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Rika(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Mur(main).toItemStack());
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(isAreaCreated()) {
            ticks--;
            if(ticks == 0) {
                ticks = 7;
                for(Player player1 : Bukkit.getOnlinePlayers()) {
                    if(area.contains(player1)) {
                        if(player1.getHealth() + 2D*2D > player1.getMaxHealth()) {
                            player1.setHealth(player1.getMaxHealth());
                        } else {
                            player1.setHealth(player1.getHealth() + 2D*2D);
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDRlOTFmY2Y1NGQ3YzBiOGVkNjIxMzAwMTNiNjBjNGFiYzUwOTlkM2NhMzkzYTJjN2E2NGNhNjg4OTUzMDJjMyJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Inoue Orihime";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @EventHandler
    public void onDamageWithShunShun(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager playerManager = main.getPlayerManager(damager.getUniqueId());
            if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime) {
                if(damager.getItemInHand().isSimilar(new ShunShun(main).toItemStack())) {
                    if(player.getHealth() + 2D > player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    } else {
                        player.setHealth(player.getHealth() + 2D);
                    }
                    if(!secondUse) {
                        secondUse = true;
                    } else {
                        secondUse = false;
                        damager.damage(1D);
                    }
                    player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§a“Shun Shun” vous régénère 1 coeur.");
                    damager.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§dVous régénérez 1 coeur à "+player.getName()+".");
                }
            }
        }
    }

    @EventHandler
    public void onBreakBlockInZone(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();

        if(area != null && area.contains(location)) {
            if(area.getCenter() == location) {
                Player inoueOrihime = super.getPlayer();
                if(inoueOrihime != null) {
                    if(inoueOrihime.hasPotionEffect(PotionEffectType.WEAKNESS))
                        inoueOrihime.removePotionEffect(PotionEffectType.WEAKNESS);
                    inoueOrihime.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*60, 0, false, false));
                    inoueOrihime.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cVotre zone a été détruite par un joueur. Vous recevez§7 Weakness 1§c pendant 1 minute !");
                }
            } else {
                event.setCancelled(true);
                deleteArea();
            }
        }
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        if(wall != null && wall.contains(block)) {
            if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime) {
                wall.getBlocks().remove(block);
            } else {
                player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§cSeul \"Inoue Orihime\" peut détruire ce bloc.");
                event.setCancelled(true);
            }

        }
    }


    @EventHandler
    public void onPlaceRika(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Location location = event.getBlockPlaced().getLocation();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        if(playerManager.hasRole() && playerManager.getRole() instanceof InoueOrihime) {
            if(player.getItemInHand().getType() == Material.STAINED_GLASS &&
                    player.getItemInHand().getItemMeta().hasDisplayName() && player.getItemInHand().getItemMeta().getDisplayName().equals("§eRika")) {
                if(bleachPlayerManager.canUsePower()) {
                    if(playerManager.getRoleCooldownRika() <= 0) {
                        createArea(location);
                        playerManager.setRoleCooldownRika(60*7);
                        player.setItemInHand(new Rika(main).toItemStack());
                        player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§dVotre zone a été placé ! Tous les joueurs dans cette zone seront soignés !");
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }

        }
    }

    private void createArea(Location location){
        area = new Cuboid(location, 5, 3);
        area.getBlocks().forEach(block -> {
                block.setType(Material.STAINED_GLASS);
                block.setData((byte)4);
        });
        Cuboid emptyArea = new Cuboid(location, 4, 2);
        emptyArea.forEach(block -> block.setType(Material.AIR));
        area.getCenter().getBlock().setType(Material.STAINED_GLASS_PANE);
        area.getCenter().getBlock().setData((byte)1);
        Bukkit.getScheduler().runTaskLater(main, () -> areaCreated = true, 5);
    }

    public void deleteArea(){
        area.forEach(block -> block.setType(Material.AIR));
        area = null;
        areaCreated = false;
    }

    public void createWall(Player player, Location location){
        if(ClassUtils.getCardinalDirection(player) != null) {
            if(ClassUtils.getCardinalDirection(player).equalsIgnoreCase("N")
                    || ClassUtils.getCardinalDirection(player).equalsIgnoreCase( "S")
                    || ClassUtils.getCardinalDirection(player).equalsIgnoreCase("NE")
                    || ClassUtils.getCardinalDirection(player).equalsIgnoreCase("SE")){
                wall = new Cuboid(location.getWorld(), location.getBlockX(), location.getBlockY()-2, location.getBlockZ()-5, location.getBlockX(), location.getBlockY()+2, location.getBlockZ()+5);
            } else {
                wall = new Cuboid(location.getWorld(), location.getBlockX()-5, location.getBlockY()-2, location.getBlockZ(), location.getBlockX()+5, location.getBlockY()+2, location.getBlockZ());
            }
            wall.forEach(block -> {
                if(block.getType() == Material.AIR) {
                    block.setType(Material.STAINED_GLASS);
                    block.setData((byte)4);
                }
            });
        }
    }

    public void deleteWall(){
        if(wall != null) {
            wall.forEach(block -> {
                if(block.getType() == Material.STAINED_GLASS) {
                    block.setType(Material.AIR);
                }
            });
            wall = null;
        }
    }

    public boolean isAreaCreated() {
        return areaCreated;
    }

}
