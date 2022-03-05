package fr.lastril.uhchost.config.modes;

import fr.lastril.uhchost.UhcHost;

import java.io.Serializable;

public class SlaveMarketConfig implements Serializable {

    private final UhcHost main = UhcHost.getInstance();

    private int diamondsPerOwners, timePerBuy;

    public SlaveMarketConfig(){
        this.diamondsPerOwners = 30;
        this.timePerBuy = 20;
    }

    public int getTimePerBuy() {
        return timePerBuy;
    }

    public void setTimePerBuy(int timePerBuy) {
        this.timePerBuy = timePerBuy;
    }

    public void setDiamondsPerOwners(int diamondsPerOwners) {
        this.diamondsPerOwners = diamondsPerOwners;
    }

    public int getDiamondsPerOwners() {
        return diamondsPerOwners;
    }
}
