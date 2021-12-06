package fr.lastril.uhchost.modes.lg.specialevent;

import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.modes.lg.LoupGarouSpecialEvent;

public class EventVaccination extends LoupGarouSpecialEvent {


    public EventVaccination() {
        super("Vaccination", 5, 1, 2);
    }

    @Override
    public void runEvent() {
        if(main.gameManager.getModes().getMode() instanceof LoupGarouMode){
            LoupGarouMode loupGarouMode = (LoupGarouMode) main.gameManager.getModes().getMode();
            loupGarouMode.getLoupGarouManager().setVaccination(true);
        }
    }
}
