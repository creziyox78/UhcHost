package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.ActionBar;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

public class NinjutsuItem extends QuickItem {

	private NarutoV2Manager narutoV2Manager;

	public NinjutsuItem(UhcHost main) {
		super(Material.NETHER_STAR);
		super.setName("§4Ninjutsu Spatio-Temporel");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_AIR || onClick.getAction() == Action.RIGHT_CLICK_BLOCK){
				if (joueur.getRole() instanceof Obito) {
					Obito obito = (Obito) joueur.getRole();
					if (!narutoV2Manager.isInSamehada(playerClick.getUniqueId())) {
						if (joueur.getRoleCooldownNinjutsu() <= 0) {
							obito.setUseNinjutsu(!obito.isUseNinjutsu());
							if(obito.isUseNinjutsu()) {
								for(PlayerManager joueurs : main.getPlayerManagerAlives()) {
									if(joueurs.getPlayer() != null) joueurs.getPlayer().hidePlayer(playerClick);
								}
							} else {
								for(PlayerManager joueurs : main.getPlayerManagerAlives()) {
									if(joueurs.getPlayer() != null) joueurs.getPlayer().showPlayer(playerClick);
								}
							}
							if(!obito.isHasUseNinjutsu()) {
								obito.setHasUseNinjutsu(true);
								new BukkitRunnable() {

									@Override
									public void run() {
										ActionBar.sendMessage(playerClick, "§eTemps d'invisibilité restant : " + new FormatTime(obito.getTimeInvisibleRemining()));
										if(obito.isUseNinjutsu()) {

											if(obito.getTimeInvisibleRemining() <= 0) {
												joueur.setRoleCooldownNinjutsu(5*60);
												obito.setTimeInvisibleRemining(60);
												for(PlayerManager joueurs : main.getPlayerManagerAlives()) {
													if(joueurs.getPlayer() != null) joueurs.getPlayer().showPlayer(playerClick);
												}
												playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cLe temps est écoulé. Vous n'êtes plus invisible aux yeux des autres joueurs.");
												obito.setHasUseNinjutsu(false);
												obito.setUseNinjutsu(false);
												cancel();
											}
											obito.setTimeInvisibleRemining(obito.getTimeInvisibleRemining() - 1);
										} else {
											joueur.setRoleCooldownNinjutsu(5*60);
											joueur.sendTimer(playerClick, joueur.getRoleCooldownNinjutsu(), NinjutsuItem.super.toItemStack());
											obito.setTimeInvisibleRemining(60);
											for(PlayerManager joueurs : main.getPlayerManagerAlives()) {
												if(joueurs.getPlayer() != null) joueurs.getPlayer().showPlayer(playerClick);
											}
											obito.setHasUseNinjutsu(false);
											obito.setUseNinjutsu(false);
											cancel();
										}
									}
								}.runTaskTimer(main, 0, 20);
							}

							playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous êtes désormais "
									+ (obito.isUseNinjutsu() ? "invisible" : "visible") + " aux yeux des autres joueurs !");
							obito.usePower(joueur);
							obito.usePowerSpecific(joueur, super.getName());
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownNinjutsu()));
						}
					} else {
						playerClick
								.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}

				}
			}
		});
	}

}
