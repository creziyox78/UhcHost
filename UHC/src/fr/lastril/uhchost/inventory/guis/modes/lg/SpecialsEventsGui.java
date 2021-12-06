package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.modes.lg.LoupGarouMode;
import fr.lastril.uhchost.modes.lg.LoupGarouSpecialEvent;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.ClickType;

public class SpecialsEventsGui extends IQuickInventory {

    private final LoupGarouMode loupGarouMode;

    public SpecialsEventsGui(LoupGarouMode loupGarouMode) {
        super(I18n.tl("guis.lg.specialsevents.name"), 1*9);
        this.loupGarouMode = loupGarouMode;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("events", taskUpdate -> {
            int index = -1;
            for(LoupGarouSpecialEvent specialsEvents : loupGarouMode.getSpecialEventList()){
                index++;
                if(specialsEvents != null){
                    if(specialsEvents.getChance() <= 0){
                        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                                .setName(specialsEvents.getName())
                                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3YTJmMjE4YTZlNmUzOGYyYjU0NWY2YzE3NzMzZjRlZjliYmIyODhlNzU0MDI5NDljMDUyMTg5ZWUifX19")
                                .setLore("§9Pourcentage:§b " + specialsEvents.getChance() +"%")
                                .setAmount(0)
                                .toItemStack(), onClick -> {
                            if(onClick.getClickType() == ClickType.LEFT){
                                if(specialsEvents.getChance() < 100)
                                    specialsEvents.setChance(specialsEvents.getChance() + 1);
                            } else if(onClick.getClickType() == ClickType.RIGHT){
                                if(specialsEvents.getChance() > 0)
                                    specialsEvents.setChance(specialsEvents.getChance() - 1);
                            }
                        },index);
                    } else {
                        inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                                .setName(specialsEvents.getName())
                                .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVmNDI1YjRkYjdkNjJiMjAwZTg5YzAxM2U0MjFhOWUxMTBiZmIyN2YyZDhiOWY1ODg0ZDEwMTA0ZDAwZjRmNCJ9fX0=")
                                .setLore("§9Pourcentage:§b " + specialsEvents.getChance() +"%")
                                .setAmount(specialsEvents.getChance() > 64 ? 64 : specialsEvents.getChance())
                                .toItemStack(), onClick -> {
                            if(onClick.getClickType() == ClickType.LEFT){
                                if(specialsEvents.getChance() < 100)
                                    specialsEvents.setChance(specialsEvents.getChance() + 1);
                            } else if(onClick.getClickType() == ClickType.RIGHT){
                                if(specialsEvents.getChance() > 0)
                                    specialsEvents.setChance(specialsEvents.getChance() - 1);
                            }
                        },index);
                    }
                }

            }
            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null)).create(), onClick -> {
                new LoupGarouGui(loupGarouMode).open(onClick.getPlayer());
            },inv.getInventory().getSize() - 1);
        });
    }
}
