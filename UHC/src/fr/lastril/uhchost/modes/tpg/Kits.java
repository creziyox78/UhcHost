package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.modes.tpg.kits.*;

public enum Kits {

    MINEUR(new MineurKit()),
    POTION(new PotionKit()),
    ARCHER(new ArcherKit()),
    ENCHANTEUR(new EnchanteurKit()),
    PYROMANE(new PyromaneKit()),
    AERIEN(new AerienKit()),
    ;

    private final KitTaupe kitTaupe;


    Kits(KitTaupe kitTaupe) {
        this.kitTaupe = kitTaupe;
    }

    public KitTaupe getKitTaupe() {
        return kitTaupe;
    }
}
