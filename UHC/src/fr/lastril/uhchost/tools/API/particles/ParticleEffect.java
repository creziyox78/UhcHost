package fr.lastril.uhchost.tools.API.particles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public abstract class ParticleEffect {

	private final int timeInTicks;

	public ParticleEffect(int timeInTicks) {
		super();
		this.timeInTicks = timeInTicks;
	}

	public int getTimeInTicks() {
		return this.timeInTicks;
	}

	public abstract void start(Player player);

	public static void playEffect(EnumParticle particle, Location loc) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			playEffect(online, particle, loc);
		}
	}
	
	public static void playEffect(EnumParticle particle, Location loc, int r, int g, int b) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			playEffect(online, particle, loc, r, g, b);
		}
	}

	public static void playEffect(Player player, EnumParticle particle, Location loc) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(),
				(float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static void playEffect(Player player, EnumParticle particle, Location loc, int r, int g, int b) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle,
				true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), r, g, b, 1.0F, 0);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
