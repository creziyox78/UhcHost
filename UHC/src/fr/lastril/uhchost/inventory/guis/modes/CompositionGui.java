package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class CompositionGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();

    public CompositionGui() {
        super("§bComposition", 9*6);
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("roles", taskUpdate -> {
            for (int i = 0; i < inv.getInventory().getSize() - 3; i++) {
                inv.setItem(new ItemStack(Material.AIR), i);
            }

            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), onClick->{
                if(UhcHost.getInstance().gameManager.getModes().getMode() instanceof ModeConfig){
                    ModeConfig modeConfig = (ModeConfig) UhcHost.getInstance().gameManager.getModes().getMode();
                    modeConfig.getGui().open(onClick.getPlayer());
                }
            },inv.getInventory().getSize() - 1);

            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
                int index = -1;
                for (Role roles : mode.getRoles()) {
                    index++;
                    if(roles.getCamp() != null) {
                        if(roles.getItem() == null){
                            inv.setItem(new QuickItem(Material.EMERALD,pl.gameManager.countRolesInComposition(roles)).setName(roles.getCamp().getCompoColor()
                                    +roles.getRoleName()).toItemStack(), onClick -> {
                                if(onClick.getClickType() == ClickType.LEFT){
                                    pl.gameManager.addRoleToComposition(roles);
                                } else if(onClick.getClickType() == ClickType.RIGHT){
                                    pl.gameManager.removeRoleToComposition(roles);
                                }
                            },index);
                        }
                        else {
                            inv.setItem(roles.getItem().setAmount(pl.gameManager.countRolesInComposition(roles)).toItemStack(), onClick -> {
                                if(onClick.getClickType() == ClickType.LEFT){
                                    pl.gameManager.addRoleToComposition(roles);
                                } else if(onClick.getClickType() == ClickType.RIGHT){
                                    pl.gameManager.removeRoleToComposition(roles);
                                }
                            },index);
                        }
                    } else {
                        inv.setItem(roles.getItem().setAmount(pl.gameManager.countRolesInComposition(roles)).toItemStack(), onClick -> {
                            if(onClick.getClickType() == ClickType.LEFT){
                                pl.gameManager.addRoleToComposition(roles);
                            } else if(onClick.getClickType() == ClickType.RIGHT){
                                pl.gameManager.removeRoleToComposition(roles);
                            }
                        },index);
                    }
                }
            }
        });


    }
}
