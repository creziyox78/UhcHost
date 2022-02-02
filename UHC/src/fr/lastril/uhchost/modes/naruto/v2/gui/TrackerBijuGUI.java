package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.biju.Biju;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Madara;
import fr.lastril.uhchost.modes.naruto.v2.roles.jubi.Obito;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;

public class TrackerBijuGUI extends IQuickInventory {

    private final UhcHost main;
    private final Obito obito;
    private final Madara madara;
    private NarutoV2Manager narutoV2Manager;

    public TrackerBijuGUI(Obito obito, Madara madara, UhcHost main) {
        super("§6Tracker de Bijû", 2*9);
        this.main = main;
        this.obito = obito;
        this.madara = madara;
        if(main.getGamemanager().getModes() != Modes.NARUTO) return;
        narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
    }

    @Override
    public void contents(QuickInventory inv) {
        int index = 0;
        for(Biju biju : narutoV2Manager.getBijuManager().getBijuListClass()){
            int finalIndex = index;
            inv.setItem(biju.getItem()
                    .setLore("",
                            "§7Cliquez pour tracker ce Bijû")
                    .toItemStack(), onClick -> {
                Biju selectedBiju = narutoV2Manager.getBijuManager().getBijuListClass().get(finalIndex);
                if(obito != null){
                    obito.setBijuTracked(selectedBiju);
                    onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traqué maintenant§e " + biju.getItem().getName() + "§e ou son porteur.");
                    onClick.getPlayer().sendMessage("§eVoici l'endroit où apparaît " + biju.getItem().getName() + "§e : " +
                            "x: " + selectedBiju.getSafeSpawnLocation().getX() +
                            "y: " + selectedBiju.getSafeSpawnLocation().getY() +
                            "z: " + selectedBiju.getSafeSpawnLocation().getZ()
                    )        ;
                    onClick.getPlayer().sendMessage("§ePremière apparition de "+ biju.getItem().getName() + "§e : " + selectedBiju.getFirstSpawn() + " minutes.");
                } else if(madara != null){
                    madara.setBijuTracked(selectedBiju);
                    onClick.getPlayer().sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous traqué maintenant§e " + biju.getItem().getName() + "§e ou son porteur.");
                    onClick.getPlayer().sendMessage("§eVoici l'endroit où apparaît " + biju.getItem().getName() + "§e : " +
                            "x: " + selectedBiju.getSafeSpawnLocation().getX() +
                            "y: " + selectedBiju.getSafeSpawnLocation().getY() +
                            "z: " + selectedBiju.getSafeSpawnLocation().getZ()
                    )        ;
                    onClick.getPlayer().sendMessage("§ePremière apparition de "+ biju.getItem().getName() + "§e : " + selectedBiju.getFirstSpawn() + " minutes.");
                }
            }, index);
            index++;
        }
    }
}
