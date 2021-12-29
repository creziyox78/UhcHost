package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Hinata;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Neji;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Danzo;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HakkeItem extends QuickItem {

	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	private int distance = 10, health = 2;

	public HakkeItem() {
		super(Material.NETHER_STAR);
		super.setName("§bHakke");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if (joueur.hasRole()) {
				if (joueur.getRole() instanceof Hinata || joueur.getRole() instanceof Neji) {
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownHakke() == 0) {
							playerClick.sendMessage(
									Messages.NARUTO_PREFIX.getMessage() + Messages.USED_POWER.getMessage());
							joueur.setRoleCooldownHakke(20 * 60);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownHakke(), playerClick.getItemInHand());
							for (Entity entity : playerClick.getNearbyEntities(distance, distance, distance)) {
								if (entity instanceof Player) {
									Player nearPlayer = (Player) entity;
									PlayerManager nearJoueur = main.getPlayerManager(nearPlayer.getUniqueId());
									if (nearPlayer != playerClick && nearPlayer.getGameMode() != GameMode.SPECTATOR && nearJoueur.isAlive()) {
										nearPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()
												+ "§bVous venez d'être frappé par l'Hakke.");
										if (nearPlayer.getHealth() - (2D * health) <= 0) {
											if(nearJoueur.getRole() instanceof Danzo){
												Danzo danzo = (Danzo) nearJoueur.getRole();
												danzo.useVie(nearJoueur, 6);
											} else {
												nearPlayer.damage(20);
											}
										} else {
											nearPlayer.setHealth(nearPlayer.getHealth() - (2D * health));
											nearPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,
													20 * 3, 0, false, false));
											nearPlayer.addPotionEffect(
													new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
										}
									}
								}
							}
							if(joueur.getRole() instanceof NarutoV2Role){
								NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
								narutoRole.usePower(joueur);
								narutoRole.usePowerSpecific(joueur, super.getName());
							}
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownHakke()));
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

}
