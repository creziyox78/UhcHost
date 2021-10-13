package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import java.util.Timer;
import java.util.TimerTask;

public class PreparePotion implements Listener {

    private final UhcHost pl;

    public PreparePotion(UhcHost pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onBrewEvent(final BrewEvent event) {
        final ItemStack ingredient = new ItemStack(event.getContents().getIngredient().getType(), 1);
        final ItemStack[] stacks = new ItemStack[3];
        for (int i = 0; i < stacks.length; i++) {
            ItemStack stack = event.getContents().getItem(i);
            if (stack != null)
                stacks[i] = new ItemStack(stack.getType(), 1);
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                if (event.getContents() != null) {
                    BrewerInventory inventory = event.getContents();
                    int size = inventory.getSize();
                    boolean shouldSetIngredientBack = false;
                    for (int i = 0; i < size; i++) {
                        ItemStack aStack = inventory.getItem(i);
                        if (aStack != null)
                            if (shouldSetIngredientBack) {
                                inventory.setItem(i, stacks[i]);
                            } else if (aStack.getType() == Material.POTION) {
                                if (!PreparePotion.this.pl.gameManager.isAllPotionsEnable()) {
                                    inventory.setItem(i, stacks[i]);
                                    shouldSetIngredientBack = true;
                                } else {
                                    Potion p = Potion.fromItemStack(aStack);
                                    if (PreparePotion.this.pl.gameManager.getDeniedPotions().contains(p)) {
                                        inventory.setItem(i, stacks[i]);
                                        shouldSetIngredientBack = true;
                                    }
                                }
                            }
                    }
                    if (shouldSetIngredientBack)
                        inventory.setIngredient(ingredient);
                }
            }
        },1L);
    }
}
