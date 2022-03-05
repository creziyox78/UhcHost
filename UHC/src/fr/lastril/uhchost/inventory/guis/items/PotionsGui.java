package fr.lastril.uhchost.inventory.guis.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.NotStart;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;

import java.util.Collections;
import java.util.ListIterator;

public class PotionsGui extends IQuickInventory {

    private final Player player;
    private final UhcHost main = UhcHost.getInstance();

    public PotionsGui(Player player) {
        super(I18n.tl("guis.potions.name", ""), 9*2);
        this.player =player;
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            ItemsCreator ic;
            if (UhcHost.getInstance().gameManager.isNotchApple()) {
                ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOn"), Collections.singletonList(I18n.tl("guis.potions.notchAppleOnLore")), 1, (byte)1);
            } else {
                ic = new ItemsCreator(Material.GOLDEN_APPLE, I18n.tl("guis.potions.notchAppleOff"), Collections.singletonList(I18n.tl("guis.potions.notchAppleOffLore")), -1, (byte)1);
            }
            inv.setItem(ic.create(), onClick -> (UhcHost.getInstance()).gameManager.setNotchApple(!(UhcHost.getInstance()).gameManager.isNotchApple()),4);


            ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.allDeactivate"),
                    Collections.singletonList(I18n.tl("guis.potions.allDeactivateLore")), 1, (byte)76);
            inv.setItem(ic.create(), onClick -> {
                (UhcHost.getInstance()).gameManager.setAllPotionsEnable(false);
                if ((UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                    this.player.sendMessage(I18n.tl("guis.potions.allPotionsDeactivate", ""));
                    return;
                }
                this.player.sendMessage(I18n.tl("guis.potions.allPotionsAlreadyDeactivate", ""));
            },11);
            ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.deactivatePotions"), Collections.singletonList(I18n.tl("guis.potions.deactivatePotionsLore")), 1, (byte)70);
            inv.setItem(ic.create(), onClick -> {
                if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                    this.player.sendMessage(I18n.tl("guis.potions.mustActivePotions", ""));
                    return;
                }
                this.player.closeInventory();
                this.player.getInventory().clear();
                UhcHost.getInstance().gameManager.setPotionsEditMode(true);
                this.player.setGameMode(GameMode.CREATIVE);
                this.player.sendMessage(I18n.tl("guis.potions.dragPotions", ""));
                this.player.sendMessage(I18n.tl("guis.potions.validAction", ""));
                this.player.getInventory().setItem(4, (new QuickItem(Material.STAINED_GLASS_PANE, (byte)13).setName(I18n.tl("guis.potions.valid", "")).onClick(onValidate -> {
                    for (ItemStack itemStack : onValidate.getPlayer().getInventory()) {
                        if (itemStack != null &&
                                itemStack.getType() == Material.POTION &&
                                !this.main.gameManager.getDeniedPotions().contains(Potion.fromItemStack(itemStack)))
                            this.main.gameManager.getDeniedPotions().add(Potion.fromItemStack(itemStack));
                    }
                    onValidate.getPlayer().setGameMode(GameMode.ADVENTURE);
                    onValidate.getPlayer().getInventory().clear();
                    onValidate.getPlayer().closeInventory();
                    this.main.gameManager.setPotionsEditMode(false);
                    NotStart.PreHosting(onValidate.getPlayer());
                }).toItemStack()));
            },12);
            ic = new ItemsCreator(Material.POTION,  I18n.tl("guis.potions.activatePotions"), Collections.singletonList(I18n.tl("guis.potions.activateLore")), 1, (byte)1);
            inv.setItem(ic.create(), onClick -> {
                if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                    this.player.sendMessage(I18n.tl("guis.potions.mustActivePotions", ""));
                } else {
                    this.player.closeInventory();
                    new DeniedPotionGui().open(player);
                }
            },14);
            ic = new ItemsCreator(Material.POTION, I18n.tl("guis.potions.allActivate"), Collections.singletonList(I18n.tl("guis.potions.allActivatePotionsLore")), 1, (byte)73);
            inv.setItem(ic.create(), onClick -> {
                if (!(UhcHost.getInstance()).gameManager.isAllPotionsEnable()) {
                    (UhcHost.getInstance()).gameManager.setAllPotionsEnable(true);
                    this.player.sendMessage(I18n.tl("guis.potions.allPotionsActivate", ""));
                } else {
                    this.player.sendMessage(I18n.tl("guis.potions.allPotionsAlreadyActivate", ""));
                }

            },15);
            inv.addRetourItem(new RulesGuiHost());
        });

    }
}
