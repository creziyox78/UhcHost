package fr.lastril.uhchost.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerManager {
	private UUID uuid;

	private Player player;

	private PlayerState playerstats;
	
	private PlayerManager playersManager;
	
	private static PlayerManager playerManager;

	private final ArrayList <Player> ingame = new ArrayList<>();

	private Player winner;
	
	public PlayerManager() {
		this.playersManager = this;
	}
	
	public PlayerManager(Player player) {
		this.playersManager = this;
		this.player = player;
		this.playerstats = PlayerState.WAITING;
		this.uuid = player.getUniqueId();
	}
	
	public boolean isInGame() {
		return ingame.contains(player);
	}
	
	public void addInGame() {
		ingame.add(player);
	}
	
	public PlayerManager getPlayersManager() {
		return playersManager;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayerState getPlayerstats() {
		return this.playerstats;
	}

	public void setPlayerstats(PlayerState playerstats) {
		this.playerstats = playerstats;
	}

	public ArrayList<Player> getIngame() {
		return this.ingame;
	}

	public Player getWinner() {
		return this.winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	
}
