package fr.lastril.uhchost.modes.lg;

import fr.lastril.uhchost.modes.lg.specialevent.EventExposed;

public enum SpecialsEvents {

    EXPOSED(EventExposed.class),
    ;

    private final Class<? extends LoupGarouSpecialEvent> specialEvent;

    SpecialsEvents(Class<? extends LoupGarouSpecialEvent> specialEvent) {
        this.specialEvent = specialEvent;
    }

    public Class<? extends LoupGarouSpecialEvent> getSpecialEvent() {
        return specialEvent;
    }
}
