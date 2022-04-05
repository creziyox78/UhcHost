package fr.lastril.uhchost.modes.bleach.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.bleach.roles.shinigamis.KisukeUrahara;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class BenihimeGui extends IQuickInventory {

    private final KisukeUrahara kisukeUrahara;

    public BenihimeGui(KisukeUrahara kisukeUrahara) {
        super("Benihime", 9*6);
        this.kisukeUrahara = kisukeUrahara;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("benihime", taskUpdate -> {
            int i = 0;
            kisukeUrahara.getPlayerLastHitted().forEach((playerManager, timeHit) -> {
                if (timeHit > System.currentTimeMillis() - 60 * 1000) {
                    if(playerManager.getPlayer() != null && playerManager.isAlive()) {
                        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(playerManager.getPlayerName()).toItemStack(), onClick -> {
                            new BenihimePlayerGui(playerManager, UhcHost.getInstance().getPlayerManager(onClick.getPlayer().getUniqueId()), kisukeUrahara)
                                    .open(kisukeUrahara.getPlayer());
                        },i);
                    }
                }
            });
        });
    }
}
