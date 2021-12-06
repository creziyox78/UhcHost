package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.modes.lg.specialevent.EventExposed;
import fr.lastril.uhchost.modes.lg.specialevent.EventVaccination;
import fr.lastril.uhchost.modes.lg.specialevent.EventZizanie;

public enum SpecialsEvents {

    EXPOSED(EventExposed.class),
    VACCINATION(EventVaccination.class),
    ZIZANIE(EventZizanie.class),
    ;

    private final Class<? extends LoupGarouSpecialEvent> specialEvent;

    SpecialsEvents(Class<? extends LoupGarouSpecialEvent> specialEvent) {
        this.specialEvent = specialEvent;
    }

    public Class<? extends LoupGarouSpecialEvent> getSpecialEvent() {
        return specialEvent;
    }
}
