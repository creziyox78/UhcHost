package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Ancien extends Role implements LGRole {

	private boolean revived;

	public Ancien() {
		super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
	}

	@Override
	public String getSkullValue() {
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
	public String getRoleName() {
		return "Ancien";
	}

	@Override
	public String getDescription() {
		return " Vous n'avez pas de pouvoir particulier.";
	}

	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
	}

	@Override
	public void checkRunnable(Player player) {
		
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	public void setRevived(boolean revived) {
		this.revived = revived;
	}

	public boolean isRevived() {
		return revived;
	}
}
