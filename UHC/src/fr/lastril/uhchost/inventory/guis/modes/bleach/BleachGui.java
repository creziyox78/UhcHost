package fr.lastril.uhchost.inventory.guis.modes.bleach;

import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.modes.CompositionGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

public class BleachGui extends IQuickInventory {


    public BleachGui() {
        super("§3Bleach", 3*9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("bleachgui", taskUpdate -> {

            inv.setItem(new ItemsCreator(Material.BOOK, I18n.tl("guis.bleach.main.composition"), null, 1).create(), onClick -> {
                new CompositionGui().open(onClick.getPlayer());
            },1);

            inv.addRetourItem(new HostConfig());
        });
    }
}
