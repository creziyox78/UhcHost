package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.player.PlayerManager;

public class BleachPlayerManager {

    private final PlayerManager playerManager;

    private boolean inKyorakuDuel, nanaoEffect;

    public BleachPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public boolean isNanaoEffect() {
        return nanaoEffect;
    }

    public void setNanaoEffect(boolean nanaoEffect) {
        this.nanaoEffect = nanaoEffect;
    }

    public void clearCancellablePower(){
        setNanaoEffect(false);
        setInKyorakuDuel(false);
    }

    public boolean isInKyorakuDuel() {
        return inKyorakuDuel;
    }

    public void setInKyorakuDuel(boolean inKyorakuDuel) {
        this.inKyorakuDuel = inKyorakuDuel;
    }

    public boolean canUsePower(){
        return !isInKyorakuDuel() && !isNanaoEffect();
    }
}
