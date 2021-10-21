package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.command.ModeSubCommand;
import fr.lastril.uhchost.modes.lg.commands.CmdSalvation;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleCommand;
import fr.lastril.uhchost.tools.API.PotionItem;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Salvateur extends Role implements LGRole, RoleCommand {

	private boolean salvate = false;

	@Override
	public String getSkullValue() {
		return null;
	}

	@Override
	public void giveItems(Player player) {
		player.getInventory().addItem(new PotionItem(PotionType.INSTANT_HEAL, 1, true).toItemStack(2));
	}

	@Override
	public void onNight(Player player) {}

	@Override
	public void onDay(Player player) {
		salvate = false;
		player.sendMessage(Messages.LOUP_GAROU_PREFIX.getPrefix() + "§BVous avez 2 minutes pour choisir la personne que vous souhaiter protéger.");
	}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {}

	@Override
	public String getRoleName() {
		return "Salvateur";
	}

	@Override
	public String getDescription() {
		return "Au début de chaque journée, vous avez la possibilité de protéger un villageois en lui conférant Résistance 1 ainsi que NoFall pendant 20 minutes avec la commande \"/lg salvation\". Vous ne pouvez pas protéger 2 fois de suite le même villageois. Vous pouvez vous protéger vous-même.";
	}


	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
	}

	@Override
	public void checkRunnable(Player player) {
		
	}


	public void setSalvate(boolean salvate) {
		this.salvate = salvate;
	}

	public boolean isSalvate() {
		return salvate;
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	@Override
	public List<ModeSubCommand> getSubCommands() {
		return Collections.singletonList(new CmdSalvation(main));
	}
}
