package fr.lastril.uhchost.inventory.guis.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.inventory.guis.RulesGui;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class PotionsGui extends Gui {

    public PotionsGui(Player player) {
        super(player, 18, I18n.tl("guis.potions.name", ""));
        ItemsCreator ic;
        if (UhcHost.getInstance().gameManager.isNotchApple()) {
            ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOn"), Collections.singletonList(I18n.tl("guis.potions.notchAppleOnLore")), 1, (byte)1);
        } else {
            ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOff"), Collections.singletonList(I18n.tl("guis.potions.notchAppleOffLore")), -1, (byte)1);
        }
        inventory.setItem(4, ic.create());
        ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.allDeactivate"), Collections.singletonList(I18n.tl("guis.potions.allDeactivateLore")), 1, (byte)76);
        inventory.setItem(11, ic.create());
        ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.deactivatePotions"), Collections.singletonList(I18n.tl("guis.potions.deactivatePotionsLore")), 1, (byte)70);
        inventory.setItem(12, ic.create());
        ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.activatePotions"), Collections.singletonList(I18n.tl("guis.potions.activateLore")), 1, (byte)1);
        inventory.setItem(14, ic.create());
        ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.allActivate"), Collections.singletonList(I18n.tl("guis.potions.allActivatePotionsLore")), 1, (byte)73);
        inventory.setItem(15, ic.create());
        ic = new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""));
        inventory.setItem(13, ic.create());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemsCreator ic;
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            switch (is.getType()) {
                case BARRIER:
                    event.getWhoClicked().closeInventory();
                    new RulesGui(this.player).show();
                    break;
                case GOLDEN_APPLE:
                    (UhcHost.getInstance()).gameManager.setNotchApple(!(UhcHost.getInstance()).gameManager.isNotchApple());
                    if ((UhcHost.getInstance()).gameManager.isNotchApple()) {
                        ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOn", ""), Collections.singletonList(I18n.tl("guis.potions.notchAppleOnLore", "")), 1, (byte)1);
                    } else {
                        ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOff", ""), Collections.singletonList(I18n.tl("guis.potions.notchAppleOffLore", "")), -1, (byte)1);
                    }
                    inventory.setItem(4, ic.create());
                    break;
                case POTION:
                    switch (is.getDurability()) {
                        case 76:
                            (UhcHost.getInstance()).gameManager.setAllPotionsEnable(false);
                            if ((UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                                this.player.sendMessage(I18n.tl("guis.potions.allPotionsDeactivate", ""));
                                break;
                            }
                            this.player.sendMessage(I18n.tl("guis.potions.allPotionsAlreadyDeactivate", ""));
                            break;
                        case 70:
                            if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                                this.player.sendMessage(I18n.tl("guis.potions.mustActivePotions", ""));
                                break;
                            }
                            this.player.closeInventory();
                            this.player.getInventory().clear();
                            UhcHost.getInstance().gameManager.setPotionsEditMode(true);
                            this.player.setGameMode(GameMode.CREATIVE);
                            this.player.sendMessage(I18n.tl("guis.potions.dragPotions", ""));
                            this.player.sendMessage(I18n.tl("guis.potions.validAction", ""));
                            this.player.getInventory().setItem(4, (new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.potions.valid", ""), null, 1, (byte)13)).create());
                            break;
                        case 1:
                            if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                                this.player.sendMessage(I18n.tl("guis.potions.mustActivePotions", ""));
                                break;
                            }
                            this.player.closeInventory();
                            new DeniedPotionGui(this.player).show();
                            break;
                        case 73:
                            if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                                (UhcHost.getInstance()).gameManager.setAllPotionsEnable(true);
                                this.player.sendMessage(I18n.tl("guis.potions.allPotionsActivate", ""));
                                break;
                            }
                            this.player.sendMessage(I18n.tl("guis.potions.allPotionsAlreadyActivate", ""));
                            break;
                    }
                    break;
            }
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

}
