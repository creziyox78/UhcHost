package fr.lastril.uhchost.modes.naruto.v2.izanami;

public enum IzanamiGoalTarget {

    WALKONEKILOMETER("§eParcourir 1 kilomètre", false),
    KILLPLAYER("§eTuer un joueur", false),
    EATGOLDENAPPLE("§eManger 5 pommes d'or", false);

    private final String description;
    private boolean done;

    IzanamiGoalTarget(String description, boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
