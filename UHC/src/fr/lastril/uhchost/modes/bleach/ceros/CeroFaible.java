package fr.lastril.uhchost.modes.bleach.ceros;

public class CeroFaible extends AbstractCero{

    public CeroFaible() {
        super("§9§lCero Faible", CeroType.CEROS_MOYEN, 5, (byte) 6);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 15 blocs/s");
        addLore("§fTaille de l'explosion :§b 2x2 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b1 coeur de dégâts");
        addLore("§e1 effet parmis les 3 suivants :");
        addLore("§b- §7Slowness pendant 7 secondes");
        addLore("§b- §fWeakness pendant 15 secondes");
        addLore("§b- §8Blindness pendant 5 secondes");
    }

    @Override
    public void action() {

    }
}
