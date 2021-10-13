package fr.lastril.uhchost.tools.API.particles;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class DoubleCircleEffect extends ParticleEffect {

	private final EnumParticle particle;
	
	public DoubleCircleEffect(int timeInTicks, EnumParticle effect) {
		super(timeInTicks);
		this.particle = effect;
	}

	@Override
	public void start(Player player) {
		new BukkitRunnable() {
			int ticks;
			
			double var;
			
			
			@Override
			public void run() {
				
				if(player.getGameMode() == GameMode.SPECTATOR) cancel();
				
				if (ticks > getTimeInTicks())
					cancel();
				
				var += Math.PI / 16;
				
				Location loc = player.getLocation();
				Location firstCircle = loc.clone().add(Math.cos(var), Math.sin(var)+ 1, Math.sin(var));
				Location secondCircle = loc.clone().add(Math.cos(var + Math.PI), Math.sin(var)+ 1, Math.sin(var + Math.PI));
				
				playEffect(particle, firstCircle);
				playEffect(particle, secondCircle);
				
				
				ticks++;
			}
		}.runTaskTimer(UhcHost.getInstance(), 0, 1);
	}

}
