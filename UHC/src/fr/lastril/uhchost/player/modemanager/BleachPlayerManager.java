package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.player.PlayerManager;

public class BleachPlayerManager {

    private final PlayerManager playerManager;

    private boolean inKyorakuDuel;

    public BleachPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public boolean isInKyorakuDuel() {
        return inKyorakuDuel;
    }

    public void setInKyorakuDuel(boolean inKyorakuDuel) {
        this.inKyorakuDuel = inKyorakuDuel;
    }

    public boolean canUsePower(){
        return !isInKyorakuDuel();
    }
}
