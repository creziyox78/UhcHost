package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.roles.LGFacadeRole;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MontreurDours extends Role implements LGRole {

	private static final int DISTANCE = 50;

	@Override
	public String getSkullValue() {
		return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmRhMzg4NjQ1Y2Y1YTdhOTNhY2NhODQyM2YxZGM2NzRlZDIxN2Q3NjJhOWZkMmZiNjIxYWYyZTY2OTRjNTcifX19";
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
	public void onNewDay(Player player) {
		boolean founded = false;
		for(Entity entity : player.getNearbyEntities(DISTANCE, DISTANCE, DISTANCE)) {
			if(entity instanceof Player) {
				Player target = (Player) entity;
				if(target.getGameMode() != GameMode.SPECTATOR) {
					if(UhcHost.getInstance().getPlayerManager(target.getUniqueId()) != null) {
						PlayerManager joueur = UhcHost.getInstance().getPlayerManager(target.getUniqueId());
						if(joueur.isAlive() && joueur.hasRole()) {
							if(!(joueur.getRole() instanceof LGFacadeRole)){
								if(joueur.getWolfPlayerManager().getLgRole().getCamp() == Camps.LOUP_GAROU|| joueur.getRole() instanceof LoupGarouBlanc) {
									Bukkit.broadcastMessage("§6Grrrrrrrrrrrrrr !");
									founded = true;
								}
							} else {
								LGFacadeRole lgFacadeRole = (LGFacadeRole) joueur.getRole();
								if(lgFacadeRole.getRoleFacade().getCamp() == Camps.LOUP_GAROU || lgFacadeRole.getRoleFacade() instanceof LoupGarouBlanc){
									Bukkit.broadcastMessage("§6Grrrrrrrrrrrrrr !");
								}
							}

						}
					}
				}
			}
		}
		if(founded) {
			for(Player players : Bukkit.getOnlinePlayers()) {
				players.playSound(players.getLocation(), Sound.WOLF_GROWL, 1, 1);
			}
		}
	}

	@Override
	public String getRoleName() {
		return "Montreur d'Ours";
	}

	@Override
	public String getDescription() {
		return "Vous avez le pouvoir de détecter les Loups-Garous à chaque début d'épisode, il y a un ou plusieurs \"Grrrr !\" en fonction du nombre de Loups-Garous se trouvant dans un rayon de 50 blocks autour de vous.";
	}

	@Override
	public ItemsCreator getItem() {
		return new ItemsCreator(Material.SKULL_ITEM, getRoleName(), Arrays.asList(""), 1, (byte) 3);
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDQ5NThkN2M2OWFlNDg0YzY1ZjMxMzQ3Y2RjYzkyYzY5ZjU0MDY4MDViNTM2NTJhNzVhOGVkNzk5ZGY3In19fQ==");
	}

	@Override
	public void checkRunnable(Player player) {
		
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

}
