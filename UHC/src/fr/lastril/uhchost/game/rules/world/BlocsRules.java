package fr.lastril.uhchost.game.rules.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.inventory.guis.rules.BlocsRulesGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BlocsRules implements Listener {

    private final UhcHost main;

    private final ItemStack[] blacklist = {
            new ItemStack(Material.STONE, 1, (byte)1),
            new ItemStack(Material.STONE, 1, (byte)5),
            new ItemStack(Material.STONE, 1, (byte)3),
            new ItemStack(Material.STONE, 1, (byte)4),
            new ItemStack(Material.STONE, 1, (byte)2),
    };

    private boolean stoneVariant;
    private boolean lava;
    private boolean flint_steal;
    private int boostXP;

    public BlocsRules(UhcHost main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onBreakOtherStone(ItemSpawnEvent event){
        if(event.getEntity().getItemStack().getType() == Material.STONE){
            if(!stoneVariant){
                ItemStack itemStack = event.getEntity().getItemStack();
                for(ItemStack itemTarget : blacklist){
                    if(itemStack.isSimilar(itemTarget)){
                        itemStack.setType(Material.COBBLESTONE);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlaceLava(PlayerBucketEmptyEvent event){
        Material bucket = event.getBucket();
        if(bucket == Material.LAVA_BUCKET){
            if(!lava){
                event.setCancelled(true);
                event.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cLa lave est désactivé ! Faites /rules pour plus d'informations.");
            }
        }
    }

    @EventHandler
    public void onFlintStealInteract(PlayerInteractEvent event){
        ItemStack itemStack = event.getItem();
        if(itemStack != null){
            if(itemStack.getType() == Material.FLINT_AND_STEEL){
                if(!flint_steal){
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Messages.PREFIX_WITH_ARROW.getMessage() + "§cLe briquet est désactivé ! Faites /rules pour plus d'informations.");
                }
            }
        }
    }

    public void dropXP(Location loc, int value) {
        ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
        orb.setExperience(value);
    }

    public IQuickInventory getGui(){
        return new BlocsRulesGui(main, this);
    }

    public boolean isStoneVariant() {
        return stoneVariant;
    }

    public void setStoneVariant(boolean stoneVariant) {
        this.stoneVariant = stoneVariant;
    }

    public boolean isLava() {
        return lava;
    }

    public void setLava(boolean lava) {
        this.lava = lava;
    }

    public boolean isFlint_steal() {
        return flint_steal;
    }

    public void setFlint_steal(boolean flint_steal) {
        this.flint_steal = flint_steal;
    }

    public int getBoostXP() {
        return boostXP;
    }

    public void setBoostXP(int boostXP) {
        this.boostXP = boostXP;
    }
}
