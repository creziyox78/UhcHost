package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.bleach.items.Ryodan;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ClassUtils;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Iba extends Role implements ShinigamiRole, RoleListener {

    private boolean inRyodan;
    private Player damaged;

    public Iba(){
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void checkRunnable(Player player) {
        super.checkRunnable(player);
        if(damaged != null){
            UhcHost.debug("Damaged not null, checking close to wall...");
            if(nextToWall(damaged)){
                UhcHost.debug("Close to wall !");
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*3, 250, false, false));
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 20*3, 250, false, false));
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));
                damaged.sendMessage(Messages.BLEACH_PREFIX.getMessage() + "§eIba vous a projeté face à un mur qui vous étourdit.");
                damaged.playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 10, 29);
                damaged = null;
            }
        }
    }

    @Override
    public void afterRoles(Player player) {
        player.setMaxHealth(player.getMaxHealth() + 4D);
        player.setHealth(player.getMaxHealth());
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Ryodan(main).toItemStack());
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
                .setSkullOwner("Dragon_Tamers");
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Iba";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }


    @EventHandler
    public void onEatApple(PlayerItemConsumeEvent event){
        if(event.getItem().getType() == Material.GOLDEN_APPLE) {
            Player player = event.getPlayer();
            PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
            if(joueur.hasRole()){
                if(joueur.getRole() instanceof Iba){
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(player.hasPotionEffect(PotionEffectType.REGENERATION)){
                                player.removePotionEffect(PotionEffectType.REGENERATION);
                            }
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20*7, 1));
                        }
                    }.runTaskLater(main, 3);
                }
            }
        }
    }
    @EventHandler
    public void onPlaceCristal(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        if(item.getType() == Material.IRON_BLOCK){
            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                if(item.getItemMeta().getDisplayName().equalsIgnoreCase("§9Ryodan")){
                    event.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onRyodanDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player target = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
            if(damagerManager.hasRole() && damagerManager.getRole() instanceof Iba){
                Iba iba = (Iba) damagerManager.getRole();
                if(iba.isInRyodan()){
                    ClassUtils.ripulseSpecificEntityFromLocation(target, damager.getLocation(), 2, 1);
                    iba.setInRyodan(false);
                    iba.damaged = target;
                    Bukkit.getScheduler().runTaskLater(main, () -> iba.damaged = null, 20*4);

                }
            }
        }
    }

    public boolean nextToWall(Player p)
    {
        for(int x = p.getLocation().getBlockX() - 1; x < x + 2; x++)
        {
            for(int z = p.getLocation().getBlockZ() - 1; z < z + 2; z++)
            {
                Block b = p.getWorld().getBlockAt(x, p.getLocation().getBlockY() + 1, z);

                if(b.getType() != Material.AIR)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInRyodan() {
        return inRyodan;
    }

    public void setInRyodan(boolean inRyodan) {
        this.inRyodan = inRyodan;
    }
}
