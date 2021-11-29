package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ModesGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();



    public ModesGui() {
        super(ChatColor.AQUA + "Modes", 9*3);

    }


    @Override
    public void contents(QuickInventory inv) {
        int index = -2;
        for (Modes mode : Modes.values()) {
            index += 3;
            QuickItem quickItem = mode.getItem();
            quickItem.getLore().add("");
            quickItem.getLore().add(pl.gameManager.getModes() == mode ? "§aActivé" : "§cDésactivé");
            if(mode.getMode() instanceof ModeConfig){
                quickItem.getLore().add("");
                quickItem.getLore().add("§6Clique droit pour configurer");
            }
            ItemStack is = quickItem.toItemStack();
            inv.setItem(is, onClick ->{
                if(onClick.getClickType() == ClickType.LEFT){
                    if(is.isSimilar(mode.getItem().toItemStack()) && mode.isAvailable()){
                        pl.gameManager.setModes(mode);
                    } else {
                        onClick.getPlayer().sendMessage(Messages.PREFIX_WITH_SEPARATION.getMessage() + "§cCe mode de jeu n'est pas encore disponible !");
                    }
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    if(is.isSimilar(mode.getItem().toItemStack()) && mode.getMode() instanceof ModeConfig && pl.gameManager.getModes() == mode){
                        ModeConfig config = (ModeConfig) mode.getMode();
                        config.getGui().open(onClick.getPlayer());
                    } else {
                        onClick.getPlayer().sendMessage(Messages.error("Ce mode n'est pas configurable ou vous ne l'avez pas séléctionné !"));
                    }
                }
            },index);
        }
        inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), inv.getInventory().getSize() - 1);
    }
}
