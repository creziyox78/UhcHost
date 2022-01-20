package fr.lastril.uhchost.modes.naruto.v2.roles.orochimaru;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KyodaigumoItem;
import fr.lastril.uhchost.modes.naruto.v2.items.MarqueMauditeItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kidomaru extends Role implements NarutoV2Role, RoleListener, MarqueMauditeItem.MarqueMauditeUser {
	
	public Kidomaru() {
        super.addRoleToKnow(Orochimaru.class);
	}


    @Override
    public void giveItems(Player player) {
    	QuickItem arc = new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
    	main.getInventoryUtils().giveItemSafely(player, arc.toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new MarqueMauditeItem(main).toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new KyodaigumoItem(main).toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.ARROW, 64));
    }

    @EventHandler
    public void onBowDamage(EntityDamageByEntityEvent event) {
    	Entity entity = event.getEntity();
    	if (entity instanceof Player) {
    	    Player player = (Player) entity;
    	    PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    	    if(event.getDamager() instanceof CaveSpider){
    	        CaveSpider caveSpider = (CaveSpider) event.getDamager();
    	        if(joueur.hasRole()){
    	            if(joueur.getRole() instanceof Kidomaru){
    	                if(caveSpider.getCustomName().equalsIgnoreCase("§5Enfant de Kyodaigumo")){
    	                    event.setCancelled(true);
                        }
                    }
                }
            }
    		if (event.getDamager() instanceof Arrow) {
    			Arrow arrow = (Arrow) event.getDamager();
    			if (arrow.getShooter() instanceof Player) {
    				Player damager = (Player) arrow.getShooter();
    				PlayerManager damagerJoueur = main.getPlayerManager(damager.getUniqueId());
    				if(damagerJoueur.hasRole()) {
    					if(damagerJoueur.getRole() instanceof Kidomaru) {
    						int value = UhcHost.getRANDOM().nextInt(100);
    						if(value <= 60) {
    							event.setDamage(event.getDamage() + ((15/100)*event.getDamage()));
    						}
                            if(value <= 20) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 0, false, false));
                                player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eUne flèche vient de toucher votre oeil. Votre vue est réduite temporairement.");
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
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
				.setTexture(
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdiMDEzNWFhOThmNjM5NzMxMjRjNDI2Y2RjOWVjZjcwMjdjYjExN2VjNTBiMzVkODE4ODY4OWJjYWExNWE3MiJ9fX0=");
	}

    @Override
    public Camps getCamp() {
        return Camps.OROCHIMARU;
    }

    @Override
    public String getRoleName() {
        return "Kidômaru";
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
		return null;
	}

    @Override
    public void onUseMarqueMaudite(UhcHost main, Player player, PlayerManager joueur) {
        if (player.hasPotionEffect(PotionEffectType.WEAKNESS))
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 0, false, false));
        joueur.setRoleCooldownMarqueMaudite(60 * 30);
        joueur.sendTimer(player, joueur.getRoleCooldownMarqueMaudite(), player.getItemInHand());
        new BukkitRunnable() {

            @Override
            public void run() {
                if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false));
                player.setMaxHealth(player.getMaxHealth() - (2D * 2D));
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.setMaxHealth(player.getMaxHealth() + (2D * 2D));
                    }
                }.runTaskLater(main, 20 * 60 * 15);
            }
        }.runTaskLater(main, 20 * 60 * 5);
        player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
    }
}