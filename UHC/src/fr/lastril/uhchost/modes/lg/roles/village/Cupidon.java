package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdCouple;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.tools.API.Livre;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class Cupidon extends Role implements LGRole, RoleCommand {

	private boolean usedPower = false;

	@Override
	public String getSkullValue() {
		return null;
	}

	@Override
	public void giveItems(Player player) {
		player.getInventory().addItem(new Livre(Enchantment.ARROW_KNOCKBACK, 1).toItemStack(), new ItemStack(Material.ARROW, 64), new ItemStack(Material.STRING, 3));
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
	public String getRoleName() {
		return "Cupidon";
	}

	@Override
	public String getDescription() {
		return " Vous n'avez pas de pouvoir particulier.";
	}

	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE4NWM5ZDdlYTM3NTUzM2RkNjBkZDQ3OGViYjE0OWExY2NkOTQ0YTRhM2ZjYTcxZDE5ZjlkNzg3YjQ2NDZmYyJ9fX0=");
	}

	@Override
	public void checkRunnable(Player player) {
		
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	public boolean isUsedPower() {
		return usedPower;
	}

	public void setUsedPower(boolean usedPower) {
		this.usedPower = usedPower;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Arrays.asList(new CmdCouple(main));
	}
}
