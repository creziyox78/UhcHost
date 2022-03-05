package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.player.PlayerManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Damage implements Listener {

	private final UhcHost pl;

	public Damage(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && !this.pl.gameManager.isPvp())
			event.setCancelled(true);
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow
				&& ((Arrow) event.getDamager()).getShooter() instanceof Player) {
			if (!this.pl.gameManager.isPvp())
				event.setCancelled(true);
			this.pl.gameManager.setLastDamager((Player) event.getEntity(),
					(Player) ((Arrow) event.getDamager()).getShooter());
			/*if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				Arrow a = (Arrow) event.getDamager();
				Player p = (Player) a.getShooter();
				if ((int) (((Player) event.getEntity()).getHealth() - event.getDamage()) > 0)
					p.sendMessage(I18n.tl("bowTouchMessage",
							event.getEntity().getName(),
							String.valueOf((int) (((Player) event.getEntity()).getHealth() - event.getDamage()))));
			}*/
		}
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
			if(GameState.isState(GameState.STARTED)){

				Player damager = (Player) event.getDamager();
				Player player = (Player) event.getEntity();
				PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
				PlayerManager damagerManager = pl.getPlayerManager(damager.getUniqueId());
				if(playerManager.isInvinsible() || damagerManager.isInvinsible()){
					event.setCancelled(true);
					return;
				}
				UhcHost.debug("damage to " + player.getName() + " by " + damager.getName());
				if(playerManager.hasRole()){
					playerManager.getRole().onDamage(damager, player);
				}
				double damages = event.getDamage();
				if (damager.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
					for (PotionEffect effect : damager.getActivePotionEffects()) {
						if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
							double damagePercentage = (effect.getAmplifier() + 1) * 1.3D + 1.0D;

							if (event.getDamage() / damagePercentage <= 1.0D) {
								damages = (effect.getAmplifier() + 1) * 3 + 1;
							} else {
								damages = (int) (event.getDamage() / damagePercentage) + (effect.getAmplifier() + 1) * 3;
							}
							break;
						}
					}
				}
				if(damages != event.getDamage()){
					event.setDamage(damages);
				}
				damagerManager.addDamages((int)event.getFinalDamage());
			}
		}



	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			PlayerManager playerManager = pl.getPlayerManager(player.getUniqueId());
			if(!this.pl.gameManager.isDamage() || playerManager.isInvinsible()){
				event.setCancelled(true);
			}
		}
	}
}
