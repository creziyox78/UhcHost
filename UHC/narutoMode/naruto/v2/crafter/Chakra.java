package fr.lastril.uhchost.modes.naruto.v2.crafter;

;

public enum Chakra {

    DOTON, SUITON, KATON, FUTON, RAITON;

    private static final Chakra[] chakra = Chakra.values();

    public static Chakra getRandomChakra() {
        return chakra[UhcHost.getRandom().nextInt(Chakra.values().length)];
    }
}
