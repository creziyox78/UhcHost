package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Kakuzu;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CorpsRapiece extends QuickItem {

	private NarutoV2Manager narutoV2Manager;
	private int distance = 20, health = 2;

	public CorpsRapiece(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§cCorps Rapiécé");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(joueur.hasRole()) {
				if(joueur.getRole() instanceof Kakuzu) {
					Kakuzu kakuzu = (Kakuzu) joueur.getRole();
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if(joueur.getRoleCooldownCorpsRapiece() <= 0) {
							joueur.setRoleCooldownCorpsRapiece(60*10);
							joueur.sendTimer(playerClick, joueur.getRoleCooldownCorpsRapiece(), playerClick.getItemInHand());

							for (Entity entity : playerClick.getNearbyEntities(distance, distance, distance)) {
								if (entity instanceof Player) {
									Player nearPlayer = (Player) entity;
									PlayerManager nearJoueur = main.getPlayerManager(nearPlayer.getUniqueId());
									if (nearPlayer != playerClick && nearPlayer.getGameMode() != GameMode.SPECTATOR && nearJoueur.isAlive()) {
										if(nearJoueur.getCamps() != joueur.getCamps()) {
											nearJoueur.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cLe corps rapiécé de Kakuzu vous a immobilisé.");
											nearJoueur.stun(nearJoueur.getPlayer().getLocation());
											Bukkit.getScheduler().runTaskLater(main, () -> nearJoueur.setStunned(false), 20*5);
										}
									}
								}
							}
							kakuzu.usePowerSpecific(joueur, super.getName());
							kakuzu.usePower(joueur);
						} else {
							playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + Messages.cooldown(joueur.getRoleCooldownCorpsRapiece()));
						}
					} else {
						playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
						return;
					}
				} else {
					playerClick.sendMessage(Messages.not("Kakuzu"));
					return;
				}
			}
		});
	}

}
