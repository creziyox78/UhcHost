package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class KarinItem extends QuickItem {
	
	private int distance = 100;
	private NarutoV2Manager narutoV2Manager;
	
	public KarinItem(UhcHost main) {
		super(Material.BOOK);
		super.setName("§6Karin");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player player = onClick.getPlayer();
			PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Karin) {
					Karin karin = (Karin) joueur.getRole();
					if(!narutoV2Manager.isInSamehada(player.getUniqueId())) {
						player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Liste des joueurs se trouvant dans un rayon de 100 blocs: ");
						for(Entity target : player.getNearbyEntities(distance, distance, distance)) {
							if(target instanceof Player) {
								Player players = (Player) target;
								if(players.getGameMode() != GameMode.SPECTATOR && main.getPlayerManager(target.getUniqueId()).isAlive()) {
									player.sendMessage("§6- " + players.getName());
								}
							}
						}
						karin.usePower(joueur);
						karin.usePowerSpecific(joueur, super.getName());
					}
				} else {
					player.sendMessage(Messages.not("Karin"));
				}
			} else {
				player.sendMessage(Messages.NOTHAVE_ROLE.getMessage());
			}
		});
	}

}
