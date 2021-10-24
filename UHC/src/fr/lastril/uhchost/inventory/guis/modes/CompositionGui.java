package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.modes.lg.LGRoles;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.tools.Items;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class CompositionGui extends Gui {

    private final UhcHost pl = UhcHost.getInstance();

    public CompositionGui(Player player) {
        super(player, 9*6, "§bComposition");
        for (int i = 0; i < inventory.getSize() - 3; i++) {
            inventory.setItem(i, new ItemStack(Material.AIR));
        }
        if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
            RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
            for (Role roles : mode.getRoles()) {
                if(roles.getCamp() != null) {
                    ItemsCreator ic;
                    if(roles.getItem() == null)
                        ic = new ItemsCreator(Material.EMERALD, roles.getRoleName(), Arrays.asList("§7Camps : "+roles.getCamp().getCompoColor() + roles.getCamp().name(), "", "§7Clique droit pour", "§7retirer de la composition.")
                                , pl.gameManager.countRolesInComposition(roles));
                    else
                        ic = roles.getItem();
                    if(roles.getSkullValue() != null)
                        inventory.addItem(Items.createHead(roles.getSkullValue(), roles.getRoleName(), pl.gameManager.countRolesInComposition(roles), Arrays.asList("§7Camps : "+roles.getCamp().getCompoColor() + roles.getCamp().name(),
                                "", "§7Clique droit pour", "§7retirer de la composition.")));
                        /*inventory.addItem(ic.setLores(Arrays.asList("§7Camps : "+roles.getCamp().getCompoColor() + roles.getCamp().name(),
                                "", "§7Clique droit pour", "§7retirer de la composition."))
                                .setAmount(pl.gameManager.countRolesInComposition(roles)).createHead(roles.getSkullValue()));*/
                    else
                        inventory.addItem(ic.setLores(Arrays.asList("§7Camps : "+roles.getCamp().getCompoColor() + roles.getCamp().name(),
                                "", "§7Clique droit pour", "§7retirer de la composition."))
                                .setAmount(pl.gameManager.countRolesInComposition(roles)).create());
                } else {
                    inventory.addItem(roles.getItem().setLores(Arrays.asList("", "§7Clique droit pour", "§7retirer de la composition.")).create());
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            for(LGRoles lgRoles : LGRoles.values()){
                try {
                    Role role = lgRoles.getRole().newInstance();
                    if(event.getClick() == ClickType.LEFT){
                        if(role.getRoleName().equalsIgnoreCase(is.getItemMeta().getDisplayName())){
                            pl.gameManager.addRoleToComposition(role);
                            new CompositionGui(player).show();
                            return;
                        }
                    } else if(event.getClick() == ClickType.RIGHT){
                        if(role.getRoleName().equalsIgnoreCase(is.getItemMeta().getDisplayName())){
                            pl.gameManager.removeRoleToComposition(role);
                            new CompositionGui(player).show();
                            return;
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
