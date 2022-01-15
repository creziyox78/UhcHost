package fr.lastril.uhchost.inventory.guis.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;

import java.util.Collections;

public class DeniedPotionGui extends IQuickInventory {

    public DeniedPotionGui() {
        super(I18n.tl("guis.deniedPotions.name", ""), 9*6);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("deniedpotions", taskUpdate ->{
            int index = 0;
            for (Potion potion : (UhcHost.getInstance()).gameManager.getDeniedPotions()) {
                ItemStack pot = potion.toItemStack(1);
                ItemMeta m = pot.getItemMeta();
                m.setLore(Collections.singletonList(I18n.tl("guis.deniedPotions.lore", "")));
                pot.setItemMeta(m);
                inv.setItem(pot, onClick -> {
                    (UhcHost.getInstance()).gameManager.removeDeniedPotion(Potion.fromItemStack(pot));
                },index);
                index++;
            }
            ItemsCreator ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back", ""), Collections.singletonList(""));
            inv.setItem(ic.create(), onClick -> new PotionsGui(onClick.getPlayer()).open(onClick.getPlayer()),53);
        });

    }
}
