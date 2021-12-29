package fr.lastril.uhchost.tools.API.particles;

import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WavesCircleEffect extends ParticleEffect {

	private final EnumParticle particle;

	public WavesCircleEffect(int timeInTicks, EnumParticle effect) {
		super(timeInTicks);
		this.particle = effect;
	}

	@Override
	public void start(Player player) {
		new BukkitRunnable(){
			double t = Math.PI/4;
			Location loc = player.getLocation();
			int ticks;
			public void run(){
				t = t + 0.1*Math.PI;
				ticks++;
				for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
					double x = t*Math.cos(theta);
					double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					double z = t*Math.sin(theta);
					loc.add(x,y,z);
					ParticleEffect.playEffect(EnumParticle.FLAME, loc);
					loc.subtract(x,y,z);

					theta = theta + Math.PI/64;

					x = t*Math.cos(theta);
					y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
					z = t*Math.sin(theta);
					loc.add(x,y,z);
					loc.subtract(x,y,z);
				}
				if (ticks > getTimeInTicks())
					cancel();
			}

		}.runTaskTimer(UhcHost.getInstance(), 0, 1);
	}

}
