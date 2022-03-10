package fr.lastril.uhchost.inventory.guis.modes.ds;

import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.modes.ds.DemonSlayerMode;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

public class DemonSlayerGui extends IQuickInventory {

    private final DemonSlayerMode demonSlayerMode;

    public DemonSlayerGui(DemonSlayerMode demonSlayerMode) {
        super(I18n.tl("guis.lg.main.name"), 9*3);
        this.demonSlayerMode = demonSlayerMode;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("dsMode", taskUpdate -> {
            inv.setItem(new ItemsCreator(Material.BOOK, I18n.tl("guis.ds.main.composition"), null, 1).create(), onClick -> {
                new DSCompositionGui(Camps.DEMONS).open(onClick.getPlayer());
            },1);

            inv.setItem(new QuickItem(Material.WATCH).setName(I18n.tl("guis.lg.main.rolestime"))
                    .setLore("§b" + new FormatTime(demonSlayerMode.getRoleAnnouncement()).toFormatString())
                    .toItemStack(), onClick -> {
                new RolesDSTimeGui(demonSlayerMode).open(onClick.getPlayer());
            }, 19);

            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null)).create(), onClick -> {
                new HostConfig().open(onClick.getPlayer());
            },inv.getInventory().getSize() - 1);
        });

    }
}
