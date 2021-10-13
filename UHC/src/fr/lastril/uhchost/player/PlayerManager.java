package fr.lastril.uhchost.player;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.manager.WolfPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

	private final UUID uuid;

	private final Map<String, Integer> cooldowns;

	private boolean alive, playedGame;

	private WolfPlayerManager wolfPlayerManager;

	private final UhcHost pl;

	private final Player player;

	private final String playerName;

	private PlayerState playerstats;

	private Role role;
	private Camps camps;
	private Location lastDisconnection, deathLocation, stunLocation;
	private long deathTime;
	private boolean stunned;

	public PlayerManager(UUID uuid) {
		this.uuid = uuid;
		this.playerstats = PlayerState.WAITING;
		this.player = Bukkit.getPlayer(uuid);
		this.pl = UhcHost.getInstance();
		this.alive = true;
		this.playedGame = false;
		this.lastDisconnection = player.getLocation();
		this.cooldowns = new HashMap<>();
		this.playerName = player.getName();
		this.setWolfPlayerManager(new WolfPlayerManager(this));
	}

	public void removeCooldowns() {
		Map<String, Integer> newCooldowns = new HashMap<>();
		for (Map.Entry<String, Integer> e : this.cooldowns.entrySet()) {
			int newCooldown = e.getValue()-1;
			if(newCooldown == 0){
				if(getPlayer() != null){
					getPlayer().sendMessage("§aLe cooldown§e " + e.getKey() + "§a est à 0. Vous pouvez réutiliser ce pouvoir.");
				}
				continue;
			}
			if(newCooldown < 0){
				continue;
			}
			newCooldowns.put(e.getKey(), newCooldown);
		}
		this.cooldowns.clear();
		this.cooldowns.putAll(newCooldowns);
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public Player getPlayer() {
		return this.player;
	}

	public PlayerState getPlayerstats() {
		return this.playerstats;
	}

	public void setPlayerstats(PlayerState playerstats) {
		this.playerstats = playerstats;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public Camps getCamps() {
		return camps;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
		this.camps = role.getCamp();
	}

	public WolfPlayerManager getWolfPlayerManager() {
		return wolfPlayerManager;
	}

	public void setWolfPlayerManager(WolfPlayerManager wolfPlayerManager) {
		this.wolfPlayerManager = wolfPlayerManager;
	}

	public boolean isDead() {
		return this.deathLocation != null;
	}

	public boolean isOnline() {
		return this.getPlayer() != null;
	}

	public boolean hasRole() {
		return getRole() != null;
	}

	public Location getDeathLocation() {
		return deathLocation;
	}

	public void setDeathLocation(Location deathLocation) {
		this.deathLocation = deathLocation;
		this.deathTime = System.currentTimeMillis();
	}

	public Location getLastDisconnection() {
		return this.lastDisconnection;
	}

	public void setLastDisconnection(Location lastDisconnection) {
		this.lastDisconnection = lastDisconnection;
	}

	public boolean isStunned() {
		return stunned;
	}

	public void setStunned(boolean stunned) {
		this.stunned = stunned;
	}

	public void stun(Location loc){
		this.stunLocation = loc;
		this.stunned = true;
	}

	public Location getStunLocation() {
		return stunLocation;
	}

	public boolean isPlayedGame() {
		return playedGame;
	}

	public void setPlayedGame(boolean playedGame) {
		this.playedGame = playedGame;
	}
}
