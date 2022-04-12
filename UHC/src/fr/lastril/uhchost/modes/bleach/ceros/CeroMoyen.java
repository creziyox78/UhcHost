package fr.lastril.uhchost.modes.bleach.ceros;

public class CeroMoyen extends AbstractCero{

    public CeroMoyen() {
        super("§a§lCero Moyen", CeroType.CEROS_MOYEN, 5, (byte) 10);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 5 blocs/s");
        addLore("§fTaille de l'explosion :§b 4x4 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b2 coeurs de dégâts");
        addLore("§e1 effet parmis les 3 suivants :");
        addLore("§b- §7Slowness pendant 7 secondes");
        addLore("§b- §fWeakness pendant 15 secondes");
        addLore("§b- §8Blindness pendant 5 secondes");
    }

    @Override
    public void action() {

    }
}
