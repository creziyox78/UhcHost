package fr.lastril.uhchost.modes.bleach.ceros;

public enum CeroType {

    CEROS_FORT(new CeroFort()),
    CEROS_MOYEN(new CeroMoyen()),
    CEROS_FAIBLE(new CeroFaible());
    ;

    private final AbstractCero cero;

    CeroType(AbstractCero cero) {
        this.cero = cero;
    }

    public AbstractCero getCero() {
        return cero;
    }
}

