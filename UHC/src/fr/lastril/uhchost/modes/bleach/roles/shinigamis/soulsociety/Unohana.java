package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Cristal;
import fr.lastril.uhchost.modes.bleach.items.Minazuki;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.modemanager.BleachPlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Unohana extends Role implements RoleListener, ShinigamiRole {

    private final List<BukkitTask> particlesTasks = new ArrayList<>();
    private Location center;
    private int healUse = 0, decountRiding = 10;
    private boolean riding;
    private final List<PlayerManager> playersRiding = new ArrayList<>();

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Cristal(main).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new Minazuki(main).toItemStack());
    }

    @Override
    public void afterRoles(Player player) {
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            if(riding){
                decountRiding--;
                if(decountRiding <= 0){
                    riding = false;
                    decountRiding=10;
                    PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                    playerManager.setRoleCooldownMinazuki(15*60);
                    playersRiding.forEach(ridingManager -> {
                        Player rided = ridingManager.getPlayer();
                        if(rided != null){
                            if(rided.hasPotionEffect(PotionEffectType.REGENERATION))
                                rided.removePotionEffect(PotionEffectType.REGENERATION);
                            rided.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*90, 2));
                            rided.sendMessage("§aUnohana vous a placé sur \"Minazuki\" celui ci vous régénère votre vie pendant 1 minute 30");
                        }
                    });
                    player.sendMessage("§9\"Minazuki\" vous confère§b Speed 3§9 et§a Jump Boost 4§9 pendant 1 minute 30. Les joueurs portés ont reçu§d Régénération 3§9.");
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*90, 2, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*90, 3, false, false));
                    playersRiding.clear();
                }
            }
        }, 0L, 20L);
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
                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODg4YzRlMjgxYTVmYjMxNTEzMWUyMzJmNjA0ODA4NWE2YWU0ODkxZWViNDZiNmZlYzMzNWM5YTIyNzMzN2UxMSJ9fX0=");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Unohana";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    @Override
    public void onDamage(Player damager, Player target) {
        if(healUse<2){
            PlayerManager joueur = main.getPlayerManager(target.getUniqueId());
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof ShinigamiRole){
                    if(target.getHealth() <= 4*2D){
                        Player player = super.getPlayer();
                        if(player!=null){
                            PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
                            if(playerManager.getRoleCooldownUnohanaHeal()<=0){
                                new ClickableMessage(super.getPlayer(), onClickMessage -> {
                                    if(target!=null && joueur.isAlive()){
                                        ClassUtils.setCorrectHealth(target, target.getHealth() + 7D*2D, false);
                                        healUse++;
                                        super.getPlayer().sendMessage(Messages.BLEACH_PREFIX.getMessage() + "Vous venez de soigner " + target.getName() + " !");
                                        playerManager.setRoleCooldownUnohanaHeal(5*60);
                                    }
                                }, Messages.BLEACH_PREFIX.getMessage() + "§3"+target.getName()+" est bas en vie !\n" + Messages.CLICK_HERE.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreakCenterCristalZone(BlockBreakEvent event){
        Block block = event.getBlock();
        Location location = block.getLocation();
        if(block.getType() == Material.STAINED_GLASS){
            if(center.distance(location) <=0.2){
                center = null;
                clearTasks();
                Player unohana = super.getPlayer();
                if(unohana != null){
                    unohana.sendMessage("§cQuelqu'un vient de détruire votre zone !");
                }
            }
        }
    }

    @EventHandler
    public void onPlaceCristal(BlockPlaceEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        BleachPlayerManager bleachPlayerManager = playerManager.getBleachPlayerManager();
        ItemStack item = event.getItemInHand();
        if(item.getType() == Material.PACKED_ICE){
            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§bCristal")){
                    if(playerManager.hasRole() && playerManager.getRole() instanceof Unohana){
                        if(bleachPlayerManager.canUsePower()){
                            if(playerManager.getRoleCooldownCristal() <= 0){
                                center = event.getBlockPlaced().getLocation().clone();
                                event.getBlockPlaced().getLocation().getBlock().setType(Material.STAINED_GLASS);
                                player.setItemInHand(new Cristal(main).toItemStack());
                                clearTasks();
                                createZone(event.getBlockPlaced().getLocation());
                                player.sendMessage("§aZone établie !");
                                playerManager.setRoleCooldownCristal(10*60);
                            } else {
                                event.setCancelled(true);
                                player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownCristal()));
                            }
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(Messages.BLEACH_PREFIX.getMessage() + Messages.CANT_USE_POWER_NOW.getMessage());
                        }
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(Messages.not("Unohana"));
                    }
                }
            }
        }
    }

    public void clearTasks(){
        particlesTasks.forEach(BukkitTask::cancel);
    }

    public void createZone(Location loc){
        Cuboid cuboid = new Cuboid(loc, 7);
        for (Block block : cuboid.getBlocks()){
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () ->
                    WorldUtils.spawnParticle(block.getLocation(), EnumParticle.VILLAGER_HAPPY),1,1);
            particlesTasks.add(task);
        }
        cuboid = cuboid.expand(Cuboid.CuboidDirection.Up, 2);
        Cuboid finalCuboid = new Cuboid(cuboid).expand(Cuboid.CuboidDirection.Down, 1);
        BukkitTask regenTask = Bukkit.getScheduler().runTaskTimer(main, () ->
                main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.SHINIGAMIS).forEach(playerManager -> {
            Player player = playerManager.getPlayer();
            if(player != null) {
                if(finalCuboid.contains(player)) {
                    if(player.hasPotionEffect(PotionEffectType.REGENERATION))
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*4, 1, false, false));
                }
            }
        }), 40, 40);
        particlesTasks.add(regenTask);
    }

    public void addRidingPlayer(PlayerManager playerManager, Player unohana){
        Player player = playerManager.getPlayer();
        if(!playersRiding.contains(playerManager)){
            switch (playersRiding.size()){
                case 0:
                    playersRiding.add(playerManager);
                    unohana.setPassenger(player);
                    unohana.sendMessage("Vous avez placés "+ player.getName()+" sur \"Minazuki\"");
                    break;
                case 1:
                    playersRiding.add(playerManager);
                    playersRiding.get(0).getPlayer().setPassenger(player);
                    unohana.sendMessage("Vous avez placés "+ player.getName()+" sur \"Minazuki\"");
                    break;
                case 2:
                    playersRiding.add(playerManager);
                    playersRiding.get(1).getPlayer().setPassenger(player);
                    unohana.sendMessage("Vous avez placés "+ player.getName()+" sur \"Minazuki\"");
                    break;
                default:
                    unohana.sendMessage("§cVous ne pouvez pas porter plus de 3 joueurs !");
                    PlayerManager unohanaManager = main.getPlayerManager(unohana.getUniqueId());
                    unohanaManager.setRoleCooldownMinazuki(15*60);
                    break;
            }
        } else {
            unohana.sendMessage("§cVous portez déjà ce joueur sur vous.");
        }

    }

    public int getRidedRemining(){
        return 3 - playersRiding.size();
    }

    public void setRiding(boolean riding) {
        this.riding = riding;
    }

    public void setDecountRiding(int decountRiding) {
        this.decountRiding = decountRiding;
    }
}
