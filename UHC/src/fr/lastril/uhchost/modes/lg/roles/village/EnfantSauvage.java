package fr.lastril.uhchost.modes.lg.roles.village;

import java.util.UUID;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EnfantSauvage extends Role implements LGRole {
	
	private UUID modele;

	@Override
	public void giveItems(Player player) {
		/*new ClickableMessage(player, onClick -> {
			new SelectModeleGUI(this).open(onClick);
		}, Messages.LG_PREFIX.getMessage()+Messages.CLICK_HERE.getMessage()+"Pour choisir votre modèle !", "§aClique pour choisir ton modèle !");*/
		
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
		return "Enfant Sauvage";
	}

	@Override
	public String getDescription() {
		//TODO
		return " Vous n'avez pas de pouvoir particulier.";
	}


	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFiODMwZWI0MDgyYWNlYzgzNmJjODM1ZTQwYTExMjgyYmI1MTE5MzMxNWY5MTE4NDMzN2U4ZDM1NTU1ODMifX19");
	}

	@Override
	public void checkRunnable(Player player) {
		
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	@Override
	public String getSkullValue() {
		return null;
	}

	@Override
	public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
		if(player.getUuid() == modele) {
			PlayerManager enfant = UhcHost.getInstance().getPlayerManager(getPlayerId());
			enfant.getWolfPlayerManager().setCamp(Camps.LOUP_GAROU);
			enfant.getWolfPlayerManager().setTransformed(true);
			if(getPlayer() != null) {
				getPlayer().sendMessage("Votre modèle est mort, vous etes donc transformé.");
			}
		}
	}
	
	public UUID getModele() {
		return this.modele;
	}

	public void setModele(UUID modele) {
		this.modele = modele;
	}

}
