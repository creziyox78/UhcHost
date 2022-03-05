package fr.lastril.uhchost.player.modemanager;

import fr.lastril.uhchost.player.PlayerManager;

public class BleachPlayerManager {

    private final PlayerManager playerManager;

    private boolean inKyorakuDuel, nanaoEffect;

    private int speedPourcentage, strengthPourcentage, resistancePourcentage, weaknessPourcentage;

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

    public int getSpeedPourcentage() {
        return speedPourcentage;
    }

    public void setSpeedPourcentage(int speedPourcentage) {
        this.speedPourcentage = speedPourcentage;
    }

    public int getStrengthPourcentage() {
        return strengthPourcentage;
    }

    public void setStrengthPourcentage(int strengthPourcentage) {
        this.strengthPourcentage = strengthPourcentage;
    }

    public int getResistancePourcentage() {
        return resistancePourcentage;
    }

    public void setResistancePourcentage(int resistancePourcentage) {
        this.resistancePourcentage = resistancePourcentage;
    }

    public int getWeaknessPourcentage() {
        return weaknessPourcentage;
    }

    public void setWeaknessPourcentage(int weaknessPourcentage) {
        this.weaknessPourcentage = weaknessPourcentage;
    }
}
