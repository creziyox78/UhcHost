package fr.lastril.uhchost.game.mode;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.lastril.uhchost.UhcHost;

public class Warzone {
	
	private UhcHost pl;
	
	private List<UUID> playersUuid;
	
	private boolean hasGoulag;
	
	public UhcHost getPl() {
		return pl;
	}
	
	private Location spawn;
	
	private Location pos1;
	
	private Location pos2;
	
	public Warzone(UhcHost pl) {
		this.pl = pl;
		this.setSpawn(new Location(Bukkit.getWorld(pl.getConfig().getString("warzone.world")), 200, 200, 200));
		this.setPos1(new Location(Bukkit.getWorld(pl.getConfig().getString("warzone.world")), 200, 200, 200));
		this.setPos2(new Location(Bukkit.getWorld(pl.getConfig().getString("warzone.world")), 200, 200, 200));
		
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public Location getPos1() {
		return pos1;
	}

	public void setPos1(Location pos1) {
		this.pos1 = pos1;
	}

	public Location getPos2() {
		return pos2;
	}

	public void setPos2(Location pos2) {
		this.pos2 = pos2;
	}

	public boolean isHasGoulag() {
		return hasGoulag;
	}

	public void setHasGoulag(boolean hasGoulag) {
		this.hasGoulag = hasGoulag;
	}

	public List<UUID> getPlayersUuid() {
		return playersUuid;
	}

	public void setPlayersUuid(List<UUID> playersUuid) {
		this.playersUuid = playersUuid;
	}

	
}
