package fr.lastril.uhchost.inventory.guis.world;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.BiomeState;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class BiomeChooseGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();

    public BiomeChooseGui() {
        super("§bChoix du biome", 3*9);

    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            int index = 10;
            for (int i = 0; i < inv.getInventory().getSize(); i++) {
                inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1,(byte) 7).toItemStack(), i);
            }
            for(BiomeState biomeState : BiomeState.values()){
                ItemStack is = biomeState.getItemBiome();
                inv.setItem(is, onClick -> {
                    pl.gameManager.setBiomeState(biomeState);
                },index);
                index++;
            }
            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"),
                    Collections.singletonList(""))).create(), onClick -> {
                new WorldGui().open(onClick.getPlayer());
            },inv.getInventory().getSize() - 1);
        });

    }
}
