package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class ModesGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();

    public ModesGui() {
        super(ChatColor.AQUA + "Modes", 9*3);

    }


    @Override
    public void contents(QuickInventory inv) {
        int index = 7;
        for (Modes mode : Modes.values()) {
            index += Modes.values().length;
            ItemStack is = mode.getItem();
            if(mode.getMode() instanceof ModeConfig){
                ItemMeta itemMeta = is.getItemMeta();
                itemMeta.setLore(Arrays.asList("", "§6Clique droit§7 pour configurer", "§7le mode de jeu."));
                is.setItemMeta(itemMeta);
            }
            inv.setItem(is, onClick ->{
                if(onClick.getClickType() == ClickType.LEFT){
                    if(is.isSimilar(mode.getItem()) && mode != Modes.SOON_1){
                        pl.gameManager.setModes(mode);
                    }
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    if(is.isSimilar(mode.getItem()) && mode.getMode() instanceof ModeConfig){
                        ModeConfig config = (ModeConfig) mode.getMode();
                        config.getGui().open(onClick.getPlayer());
                    }
                }
            },index);
        }
        inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), inv.getInventory().getSize() - 1);
    }
}
