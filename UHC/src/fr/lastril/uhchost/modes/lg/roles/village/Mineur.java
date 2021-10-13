package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.API.Livre;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Mineur extends Role implements LGRole {
	
	public Mineur() {
		super.addEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false), When.START);
	}

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA1NjJjNTEzMDhiNTE1Nzg1Mjc4NmIxNDI0ZDFhNTJlMTNmOThiNWUzZWY1Y2M3YTgwOTBhZDc4OTM5MDI3In19fQ==";
	}

	@Override
	public String getRoleName() {
		return "Mineur";
	}

	@Override
	public String getDescription() {
		return "Pour ce faire, vous disposez d'un un livre efficiency 3 et de l'effet Haste 1.";
	}


	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	@Override
	public void giveItems(Player player) {
		player.getInventory().addItem(new Livre(Enchantment.DIG_SPEED, 3).toItemStack());
	}

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
		//return new QuickItem(Material.GOLD_PICKAXE).setName("§a"+getRoleName());
	}

}
