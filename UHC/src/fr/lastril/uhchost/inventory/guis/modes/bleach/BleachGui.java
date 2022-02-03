package fr.lastril.uhchost.inventory.guis.modes.bleach;

import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.modes.bleach.BleachMode;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

public class BleachGui extends IQuickInventory {

    private final BleachMode bleachMode;

    public BleachGui(BleachMode bleachMode) {
        super("§3Bleach", 3*9);
        this.bleachMode = bleachMode;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("bleachgui", taskUpdate -> {

            inv.setItem(new ItemsCreator(Material.BOOK, I18n.tl("guis.bleach.main.composition"), null, 1).create(), onClick -> {
                new BleachCompositionGui(Camps.SHINIGAMIS).open(onClick.getPlayer());
            },1);

            inv.setItem(new QuickItem(Material.WATCH).setName(I18n.tl("guis.lg.main.rolestime"))
                    .setLore("§b" + new FormatTime(bleachMode.getRoleAnnouncement()).toFormatString())
                    .toItemStack(), onClick -> {
                new RolesBleachTimeGui(bleachMode).open(onClick.getPlayer());
            }, 3);

            inv.addRetourItem(new HostConfig());
        });
    }
}
