package fr.lastril.uhchost.modes.lg;

public abstract class LoupGarouSpecialEvent {

    private final String name;
    private int chance;

    public LoupGarouSpecialEvent(String name, int chance){
        this.name = name;
        this.chance = chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getChance() {
        return chance;
    }

    public String getName() {
        return name;
    }
}
