package fr.lastril.uhchost.modes.bleach.ceros;

public class CeroFort extends AbstractCero{

    public CeroFort() {
        super("§c§lCero Fort", CeroType.CEROS_FORT, 10, (byte) 1);
        addLore("§eEnvoie des lasers de particules.");
        addLore("");
        addLore("§fVitesse :§b 10 blocs/s");
        addLore("§fTaille de l'explosion :§b 7x7 blocs");
        addLore("§fEffets : ");
        addLore("§b- §b3 coeurs de dégâts");
        addLore("§b- §bEnflammé pendant 10 secondes (sans pouvoir s'éteindre)");
    }

    @Override
    public void action() {

    }
}
