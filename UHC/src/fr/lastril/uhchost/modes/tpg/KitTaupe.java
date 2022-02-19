package fr.lastril.uhchost.modes.tpg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KitTaupe {

    private String name;
    private final UhcHost main = UhcHost.getInstance();
    private final List<ItemStack> itemStacks = new ArrayList<>();

    public KitTaupe(String name){
        this.name = name;
    }

    public void giveItems(Player player){
        for(ItemStack itemStack : itemStacks){
            main.getInventoryUtils().giveItemSafely(player, itemStack);
        }
        player.sendMessage(Messages.TAUPE_GUN_PREFIX.getMessage() + "§aKit reçu !");
    }

    public void addItem(ItemStack itemStack){
        itemStacks.add(itemStack);
    }

    public String getName() {
        return name;
    }
}
