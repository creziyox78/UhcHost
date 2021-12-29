package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Jugo;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Karin;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Sasuke;
import fr.lastril.uhchost.modes.naruto.v2.roles.taka.Suigetsu;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;

public class KarinTask extends BukkitRunnable {

	private final UhcHost main;
	private final Karin karin;

	public KarinTask(UhcHost main, Karin karin) {
		this.main = main;
		this.karin = karin;
	}

	@Override
	public void run() {
		for (PlayerManager joueur : main.getPlayerManagerAlives()) {
			if(joueur.hasRole() && joueur.isAlive()){
				if(joueur.getPlayer() != null){
					if(karin.getPlayer() != null){
						if (joueur.getPlayer() != karin.getPlayer()) {
							if(karin.getPlayer().getWorld() == joueur.getPlayer().getWorld()){
								if(joueur.getPlayer().getLocation().distance(karin.getPlayer().getLocation()) <= 20){

									if(joueur.getRole() instanceof Sasuke || joueur.getRole() instanceof Jugo || joueur.getRole() instanceof Suigetsu) {
										karin.getJoueurUnknow().putIfAbsent(joueur, 2*60);
									} else {
										karin.getJoueurUnknow().putIfAbsent(joueur, karin.getTimer());
									}
									karin.getJoueurUnknow().put(joueur, karin.getJoueurUnknow().get(joueur) - 1);
									if(karin.getJoueurUnknow().get(joueur) != null){
										if(karin.getJoueurUnknow().get(joueur) == 0) {
											UhcHost.debug("Karin player (" + karin.getPlayer().getName() + ") know: " + joueur.getPlayerName());
											karin.getPlayer().sendMessage("§6" + joueur.getPlayerName() + " est " + joueur.getRole().getRoleName() + ".");
										}
									}
								}
							}

						}
					}
				}
			}
		}
	}

}
