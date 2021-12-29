package fr.lastril.uhchost.modes.naruto.v2.tasks;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class SakuraTask extends BukkitRunnable {

	private final UUID sakura;
	
	public SakuraTask(UUID sakura) {
		this.sakura = sakura;
	}

	@Override
	public void run() {
		if(Bukkit.getPlayer(sakura) != null) {
			Player sakuraPlayer = Bukkit.getPlayer(sakura);
			sakuraPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*7*60, 0, false, false));
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
				new SakuraTask(sakura).runTask(UhcHost.getInstance());
			}
		}.runTaskLater(UhcHost.getInstance(), (20*60*4)+(20*60*7));
	}

}
