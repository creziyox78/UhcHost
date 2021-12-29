package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Hinata;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Neji;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ByakuganItem extends QuickItem {

	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	private int distance = 30;

	public ByakuganItem() {
		super(Material.NETHER_STAR);
		super.setName("§bByakugan");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if (joueur.hasRole()) {
				if (joueur.getRole() instanceof Hinata || joueur.getRole() instanceof Neji) {
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownByakugan() == 0) {
							playerClick.sendMessage(
									Messages.NARUTO_PREFIX.getMessage() + "§bJoueurs se trouvant à 60 blocs de vous.");
							joueur.setRoleCooldownByakugan(20*60);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownByakugan(), playerClick.getItemInHand());
							for (Entity entity : playerClick.getNearbyEntities(distance, distance, distance)) {
								if (entity instanceof Player) {
									Player nearPlayer = (Player) entity;
									PlayerManager joueurs = main.getPlayerManager(nearPlayer.getUniqueId());
									if(joueurs.isAlive() || nearPlayer.getGameMode() != GameMode.SPECTATOR){
										playerClick.sendMessage(getCardinalDirection(playerClick, nearPlayer));
									}
								}
							}
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownByakugan()));
						}

					} else {
						playerClick.sendMessage(
								Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				} else {
					playerClick.sendMessage(Messages.not("Hinata ou Neji"));
				}
			}
				
			
		});
	}

	public String getCardinalDirection(Player player, Player entity) {
		Vector playerToEntity = entity.getLocation().clone().subtract(player.getLocation()).toVector();
		Vector playerLooking = player.getLocation().getDirection();
		double x1 = playerToEntity.getX();
		double z1 = playerToEntity.getZ();
		double x2 = playerLooking.getX();
		double z2 = playerLooking.getZ();
		double distance = player.getLocation().distance(entity.getLocation());
		String message = "§3";
		double angle = Math.atan2(x1*z2-z1*x2, x1*x2+z1*z2)*180/Math.PI;
		if(angle >= -45 && angle < 45) {
			message += entity.getName() + " : Position - NORD ";
		} else if(angle >= 45 && angle < 135) {
			message += entity.getName() + " : Position - OUEST ";
		} else if(angle >= 135 && angle <= 180 || angle >= -180 && angle < -135) {
			message += entity.getName() + " : Position - SUD ";
		} else if(angle >= -135 && angle < -45) {
			message += entity.getName() + " : Position - EST ";
		}
		if(distance < 0)
			distance *= -1;
		message += "❘ Distance - " + (int) distance + "m";
		
		
		return message;
	}

}
