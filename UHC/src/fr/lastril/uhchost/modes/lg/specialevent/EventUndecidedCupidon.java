package fr.lastril.uhchost.modes.lg.specialevent;

import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.modes.lg.LoupGarouSpecialEvent;

public class EventUndecidedCupidon extends LoupGarouSpecialEvent {


    public EventUndecidedCupidon() {
        super("Cupidon Indécis", 10, 1, 2);
    }

    @Override
    public void runEvent() {
        if(main.gameManager.getModes().getMode() instanceof LoupGarouMode){
            LoupGarouMode loupGarouMode = (LoupGarouMode) main.gameManager.getModes().getMode();
            loupGarouMode.getLoupGarouManager().setRandomCouple(!loupGarouMode.getLoupGarouManager().isRandomCouple());
        }
    }
}
