package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class LoupGarouGrimeur extends Role implements LGRole {

	public LoupGarouGrimeur() {
		super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.NIGHT);
		super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*1, 0, false, false), When.AT_KILL);
		super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60*1, 0, false, false), When.AT_KILL);
	}

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmNjYzI5NmNkMTcxYzE1OGVlYzkzZWMwM2M1YTY1ZWFkYzUzODA3ZTM0N2VkYTJhMzM0YjY3MDM0NTg5N2E1OCJ9fX0=";
	}

	@Override
	public String getRoleName() {
		return "Loup-Garou";
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
		return new ItemsCreator(Material.SKULL_ITEM, getRoleName(), Arrays.asList(""), 1, (byte) 3);
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===");
	}

	@Override
	public Camps getCamp() {
		return Camps.LOUP_GAROU;
	}

}
