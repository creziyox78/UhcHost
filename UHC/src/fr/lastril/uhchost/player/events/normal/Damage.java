package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
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
			Player player = (Player) event.getEntity();
			if(pl.getPlayerManager(player.getUniqueId()).hasRole()){
				pl.getPlayerManager(player.getUniqueId()).getRole().onDamage((Player) event.getDamager(), player);
			}
		}

		Player damager = (Player) event.getDamager();
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
		event.setDamage(damages);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && !this.pl.gameManager.isDamage())
			event.setCancelled(true);
	}
}
