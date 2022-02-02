package fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.SamehadaItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kisame extends Role implements NarutoV2Role, RoleListener {
	
	private int healthAfterDamage = 50;
	
	private int damage;
	
	private int effectTime = 20*40;
	
	public Kisame() {
		super.addRoleToKnow(Itachi.class);
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
	}
	
    @Override
    public void giveItems(Player player) {
    	player.getInventory().addItem(new Livre(Enchantment.DEPTH_STRIDER, 3).toItemStack());
		player.getInventory().addItem(new SamehadaItem().toItemStack());
    }
    
    @Override
    public void onDamage(Player player, Player target) {

		if(player.getItemInHand().hasItemMeta()) {
			if(player.getItemInHand().getItemMeta().hasDisplayName()){
				if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§cSamehada")){
					if(main.getGamemanager().getModes() != Modes.NARUTO) return;
					NarutoV2Manager narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
					if(!narutoV2Manager.isInSamehada(target.getUniqueId())) {
						narutoV2Manager.addInSamehada(target.getUniqueId());
						target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous avez été frappé avec §cSamehada§e. Vous ne pouvez plus utiliser vos objets pendant 1 minute.");
						new BukkitRunnable() {
							int timer = 60;
							@Override
							public void run() {
								ActionBar.sendMessage(target, "§aEffet de Samehada : §e" + new FormatTime(timer));
								timer--;
								if(timer <= 0) {
									ActionBar.sendMessage(target, "§cVous n'êtes plus sous l'effet de Samehada.");
									target.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§aVous pouvez utiliser à nouveau vos objets.");
									narutoV2Manager.removeInSamehada(target.getUniqueId());
									cancel();
								}
							}
						}.runTaskTimer(main, 0, 20L);
					}
				}
			}

		}
    }
    
    @EventHandler
    public void onDamageCompte(EntityDamageByEntityEvent event) {
    	if(event.getEntity() instanceof Player) {
    		if(event.getDamager() instanceof Player) {
    			Player player = (Player) event.getDamager();
    			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
    			if(joueur.hasRole()) {
    				if(joueur.getRole() instanceof Kisame) {
    					Kisame kisame = (Kisame) joueur.getRole();
    					kisame.setDamage(kisame.getDamage() + (int) event.getFinalDamage());
    					if(kisame.getDamage() >= getHealthAfterDamage()) {
    						kisame.setDamage(0);
    						if(player.hasPotionEffect(PotionEffectType.REGENERATION))
    			    			player.removePotionEffect(PotionEffectType.REGENERATION);
    			    		if(player.hasPotionEffect(PotionEffectType.ABSORPTION))
    			    			player.removePotionEffect(PotionEffectType.ABSORPTION);
    			    		if(player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
    			    			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
    			    		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, effectTime, 1, false, false));
    			    		player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*5, 1, false, false));
    			    		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, effectTime, 1, false, false));
    			    		player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§eVous venez d'infliger 50 coeurs de dégâts. Vous recevez Absorption II, §dRégénération II§e et§6Fire Resistance II§e.");
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
	public void checkRunnable(Player player) {
		super.checkRunnable(player);
		if(player.getLocation().getBlock().getType().name().contains("WATER")) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20, 0, false, false));
		}
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if(joueur.hasRole() && joueur.isAlive()){
			if(joueur.getRole() instanceof Kisame){
				Kisame kisame = (Kisame) joueur.getRole();
				if(joueur.getPlayer() != null) {
					ActionBar.sendMessage(joueur.getPlayer(), "§6Dégâts infligé: " + kisame.getDamage() + "/" + kisame.getHealthAfterDamage());
				}
			}
		}

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
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU0MDkzM2ZmYzkzNGY1NjNjZjBmY2ViMzJkMDY3YTlmOTVkNDQyMDQ3OTM1MTU0ZWQ1Mzg4NWE5N2JiZWFlMyJ9fX0=");
	}

    @Override
    public Camps getCamp() {
        return Camps.AKATSUKI;
    }

    @Override
    public String getRoleName() {
        return "Kisame";
    }

    @Override
    public String getDescription() {
		return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }
    
    @Override
	public Chakra getChakra() {
		return Chakra.SUITON;
	}

	public int getHealthAfterDamage() {
		return healthAfterDamage;
	}

	public void setHealthAfterDamage(int healthAfterDamage) {
		this.healthAfterDamage = healthAfterDamage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
