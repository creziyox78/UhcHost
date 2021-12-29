package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.AkimichiItem;
import fr.lastril.uhchost.modes.naruto.v2.items.BouletHumainItem;
import fr.lastril.uhchost.modes.naruto.v2.items.DecuplementPartielItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.DoubleCircleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Choji extends Role implements NarutoV2Role, RoleListener {
	
	
	public Choji() {
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addRoleToKnow(Ino.class);
	}
	
    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new DecuplementPartielItem().toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new BouletHumainItem().toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new AkimichiItem().toItemStack());
		main.getInventoryUtils().giveItemSafely(player, new AkimichiItem().toItemStack());
		main.getInventoryUtils().giveItemSafely(player, new AkimichiItem().toItemStack());
    }

    @Override
	public void checkRunnable(Player player) {
    	super.checkRunnable(player);
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(joueur.hasRole() && joueur.isAlive()) {
			if(joueur.getRole() instanceof Choji) {
				if(player.getFoodLevel() >= 18) {
	    			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*2, 0, false, false));
	    		}
	    		if(player.getFoodLevel() <= 10) {
	    			player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0, false, false));
	    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false));
	    		} else if(player.getFoodLevel() > 10) {
	    			if(player.hasPotionEffect(PotionEffectType.HUNGER)) {
	    				player.removePotionEffect(PotionEffectType.HUNGER);
	    			}
	    			if(player.hasPotionEffect(PotionEffectType.SLOW)) {
	    				player.removePotionEffect(PotionEffectType.SLOW);
	    			}
	    		}
			}
		}
    }
    
    
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
    	Player player = event.getPlayer();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(joueur.isAlive() && joueur.hasRole()) {
			if(joueur.getRole() instanceof Choji) {
				ItemStack item = event.getItem();
				if(item.isSimilar(new AkimichiItem().toItemStack())) {
					if(player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
						player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*30, 1, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*30, 0, false, false));
					new DoubleCircleEffect(20 * 60, EnumParticle.WATER_SPLASH).start(player);
					player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aCe steak vous octroit une force à toute épreuve !");
					new BukkitRunnable() {
						
						@Override
						public void run() {
							player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§7L'indigestion commence à se faire ressentir...");
							player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*60, 0, false, false));
						}
					}.runTaskLater(main, 20*60);
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzM3NmUwMDcwOTg0ZWY3YjFkMDkxOTYxMWU1M2Y1ZDE1NGViN2FlOWZjZTkzMGY3OGJjZDhhYTZkMDk0MzNlMyJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Chôji";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return Chakra.DOTON;
	}
}
