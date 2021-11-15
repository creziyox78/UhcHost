package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.enums.ResurectType;
import fr.lastril.uhchost.player.PlayerManager;

public class NarutoPlayerManager {

	private final PlayerManager playerManager;
	
	private ResurectType resurectType;
	
	public NarutoPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public ResurectType getResurectType() {
		return this.resurectType;
	}

	public void setResurectType(ResurectType resurectType) {
		this.resurectType = resurectType;
	}
}
