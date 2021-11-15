package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.biju.Biju;
import fr.maygo.uhc.modes.naruto.v2.biju.bijus.*;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Madara;
import fr.maygo.uhc.modes.naruto.v2.roles.jubi.Obito;

public class TrackerBijuGUI extends IQuickInventory {

    private final UhcHost main;
    private final Obito obito;
    private final Madara madara;

    public TrackerBijuGUI(Obito obito, Madara madara, UhcHost main) {
        super("§6Tracker de Bijû", 2 * 9);
        this.main = main;
        this.obito = obito;
        this.madara = madara;
    }

    @Override
    public void contents(QuickInventory inv) {
        int index = 0;
        for (Biju biju : main.getNarutoV2Manager().getBijuManager().getBijuListClass()) {
            int finalIndex = index;
            inv.setItem(biju.getItem()
                    .setLore("",
                            "§7Cliquez pour tracker ce Bijû")
                    .toItemStack(), onClick -> {
                Biju selectedBiju = main.getNarutoV2Manager().getBijuManager().getBijuListClass().get(finalIndex);
                if (obito != null) {
                    obito.setBijuTracked(selectedBiju);
                    onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traqué maintenant§e " + biju.getItem().getName() + "§e ou son porteur.");
                    onClick.getPlayer().sendMessage("§eVoici l'endroit où apparaît " + biju.getItem().getName() + "§e : " +
                            "x: " + selectedBiju.getSafeSpawnLocation().getX() +
                            "y: " + selectedBiju.getSafeSpawnLocation().getY() +
                            "z: " + selectedBiju.getSafeSpawnLocation().getZ()
                    );
                    onClick.getPlayer().sendMessage("§ePremière apparition de " + biju.getItem().getName() + "§e : " + selectedBiju.getFirstSpawn() + " minutes.");
                } else if (madara != null) {
                    madara.setBijuTracked(selectedBiju);
                    onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traqué maintenant§e " + biju.getItem().getName() + "§e ou son porteur.");
                    onClick.getPlayer().sendMessage("§eVoici l'endroit où apparaît " + biju.getItem().getName() + "§e : " +
                            "x: " + selectedBiju.getSafeSpawnLocation().getX() +
                            "y: " + selectedBiju.getSafeSpawnLocation().getY() +
                            "z: " + selectedBiju.getSafeSpawnLocation().getZ()
                    );
                    onClick.getPlayer().sendMessage("§ePremière apparition de " + biju.getItem().getName() + "§e : " + selectedBiju.getFirstSpawn() + " minutes.");
                }
            }, index);
            index++;
        }
    }
}
