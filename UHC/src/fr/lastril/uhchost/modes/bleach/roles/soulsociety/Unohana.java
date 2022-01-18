package fr.lastril.uhchost.modes.bleach.roles.soulsociety;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Cristal;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.Cuboid;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.world.WorldUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private int healUse = 0;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Cristal(main).toItemStack());
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
        return "Unohana";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
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
                                        target.setHealth(target.getMaxHealth());
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
        if(location == center){
            center = null;
            clearTasks();
        }
    }

    @EventHandler
    public void onPlaceCristal(BlockPlaceEvent event){
        Player player = event.getPlayer();
        PlayerManager playerManager = main.getPlayerManager(player.getUniqueId());
        ItemStack item = event.getItemInHand();
        if(item.getType() == Material.PACKED_ICE){
            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§bCristal")){
                    if(playerManager.hasRole() && playerManager.getRole() instanceof Unohana){
                        if(playerManager.getRoleCooldownCristal() <= 0){
                            event.getBlock().setType(Material.STAINED_GLASS);
                            createZone(event.getBlockPlaced().getLocation());
                        } else {
                            player.sendMessage(Messages.cooldown(playerManager.getRoleCooldownCristal()));
                        }
                    } else {
                        player.sendMessage(Messages.not("Unohana"));
                    }
                    event.setCancelled(true);
                }
            }
        }

    }

    public void clearTasks(){
        particlesTasks.forEach(BukkitTask::cancel);
    }

    public void createZone(Location loc){
        Cuboid cuboid = new Cuboid(loc, 7);
        center = cuboid.getCenter();
        for (Block block : cuboid.getBlocks()){
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(main, () ->
                    WorldUtils.spawnParticle(block.getLocation(), EnumParticle.VILLAGER_HAPPY),1,1);
            particlesTasks.add(task);
        }
        BukkitTask regenTask = Bukkit.getScheduler().runTaskTimer(main, () ->
                main.getGamemanager().getModes().getMode().getModeManager().getPlayerManagersWithCamps(Camps.SHINIGAMIS).forEach(playerManager -> {
            Player player = playerManager.getPlayer();
            if(player != null) {
                if(cuboid.contains(player)) {
                    if(player.hasPotionEffect(PotionEffectType.REGENERATION))
                        player.removePotionEffect(PotionEffectType.REGENERATION);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*4, 1, false, false));
                }
            }
        }), 40, 40);
        particlesTasks.add(regenTask);
    }
}
