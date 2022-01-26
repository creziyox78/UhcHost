package fr.lastril.uhchost.enums;

public enum WorldState {

    JOUR, NUIT
    ;
    private static WorldState worldState = JOUR;

    public static WorldState getWorldState() {
        return worldState;
    }

    public static void setWorldState(WorldState worldState) {
        WorldState.worldState = worldState;
    }

    public static boolean isWorldState(WorldState worldState){
        return WorldState.worldState == worldState;
    }
}
