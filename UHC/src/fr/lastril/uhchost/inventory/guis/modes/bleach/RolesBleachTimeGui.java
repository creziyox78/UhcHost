package fr.lastril.uhchost.inventory.guis.modes.bleach;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.bleach.BleachMode;
import fr.lastril.uhchost.tools.API.FormatTime;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class RolesBleachTimeGui extends IQuickInventory {

    private BannerCreator bc;
    private ItemsCreator ic;
    private final BleachMode bleachMode;
    public RolesBleachTimeGui(BleachMode bleachMode) {
        super(I18n.tl("guis.bleach.rolestime.name"), 1*9);
        this.bleachMode = bleachMode;
    }


    @Override
    public void contents(QuickInventory inv) {


        inv.updateItem("update", taskUpdate -> {
            bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);

            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 0);
            bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 1);
            bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.RED);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 2);
            bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);



            ic = new ItemsCreator(Material.PAPER,
                    "§e" + new FormatTime(bleachMode.getRoleAnnouncement()).toFormatString(),
                    Arrays.asList(I18n.tl("guis.lg.rolestime.lore"), I18n.tl("guis.lg.rolestime.lore1")));
            inv.setItem(ic.create(), onClick-> {
                if(UhcHost.getInstance().gameManager.getModes().getMode() instanceof ModeConfig){
                    ModeConfig modeConfig = (ModeConfig) UhcHost.getInstance().gameManager.getModes().getMode();
                    modeConfig.getGui().open(onClick.getPlayer());
                }
            },4);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 6);
            bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 7);
            bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
            bc.setBaseColor(DyeColor.GREEN);
            inv.setItem(bc.create(), onClick -> {
                String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
                int value = bleachMode.getRoleAnnouncement() + Integer.parseInt(bannerName) * 60;
                bleachMode.setRoleAnnouncement(value);
            }, 8);
        });
    }
}
