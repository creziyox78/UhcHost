package fr.lastril.uhchost.modes.naruto.v2.crafter;

import fr.lastril.uhchost.UhcHost;

public enum Chakra {

    DOTON, SUITON, KATON, FUTON, RAITON;

    private static Chakra[]chakra=Chakra.values();

    public static Chakra getRandomChakra() {
        return chakra[UhcHost.getRANDOM().nextInt(Chakra.values().length)];
    }
}
