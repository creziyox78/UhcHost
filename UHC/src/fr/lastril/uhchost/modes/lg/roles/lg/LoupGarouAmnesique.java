package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;

public class LoupGarouAmnesique extends Role implements LGRole {

	private boolean damaged = false;

	public LoupGarouAmnesique() {
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
		super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*1, 0, false, false), When.AT_KILL);
		super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60*1, 0, false, false), When.AT_KILL);
	}

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Q5ZDJjNzU3ODVmMTIzODk4N2JiMTFhNDQyOTcyYTg2ZGFlYzk2NjJhYzU2YmJmZWEyZDgzZGI5NjJlMWFjMyJ9fX0=";
	}

	@Override
	public String getRoleName() {
		return "Loup-Garou Amnésique";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void giveItems(Player player) {}

	@Override
	public void onNight(Player player) {}

	@Override
	public void onDay(Player player) {}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {}

	@Override
	public void checkRunnable(Player player) {}

	@Override
	public ItemsCreator getItem() {
		return new ItemsCreator(Material.SKULL_ITEM, getRoleName(), Collections.singletonList(""), 1, (byte) 3);
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.LOUP_GAROU;
	}

	@Override
	public void onDamage(Player damager, Player target) {
		PlayerManager damagerManager = main.getPlayerManager(damager.getUniqueId());
		PlayerManager playerManager = main.getPlayerManager(target.getUniqueId());
		if(!damaged){
			if(damagerManager.hasRole() && playerManager.hasRole()){
				if(damagerManager.getCamps() == Camps.LOUP_GAROU && playerManager.getRole() instanceof LoupGarouAmnesique){
					damaged = true;
					Bukkit.getScheduler().runTaskLater(main, () -> {

					}, 20*60*5);
				}
			}
		}

	}

	public boolean isDamaged() {
		return damaged;
	}

	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}
}
