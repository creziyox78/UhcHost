package fr.lastril.uhchost.modes.sm;

public enum MarketStatus {

    WAITING,
    STARTING,
    STARTED,
    COOLDOWN,
    FINISHED
    ;

    private static MarketStatus marketStatus = WAITING;

    public static void setMarketStatus(MarketStatus marketStatus) {
        MarketStatus.marketStatus = marketStatus;
    }

    public boolean isMarketStatus(MarketStatus marketStatus){
        return MarketStatus.marketStatus == marketStatus;
    }

    public static MarketStatus getInstance() {
        return marketStatus;
    }
}
