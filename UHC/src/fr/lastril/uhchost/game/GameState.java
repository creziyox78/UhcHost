package fr.lastril.uhchost.game;

public enum GameState {
	REBUILDING, LOBBY, STARTING, TELEPORTING, PRESTART, STARTED, ENDED;

	private static GameState currentState;

	public static GameState getCurrentState() {
		return currentState;
	}

	public static void setCurrentState(GameState state) {
		currentState = state;
	}

	public static boolean isState(GameState state) {
		return (currentState == state);
	}

}
