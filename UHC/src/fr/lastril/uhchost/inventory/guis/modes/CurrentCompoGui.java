package fr.lastril.uhchost.inventory.guis.modes;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CurrentCompoGui extends IQuickInventory {

    private final UhcHost main;

    public CurrentCompoGui(UhcHost main) {
        super("§bComposition", 6*9);
        this.main = main;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("roles", onUpdate -> {
            for (int i = 0; i < inv.getInventory().getSize() - 3; i++) {
                inv.setItem(new ItemStack(Material.AIR), i);
            }
            List<String> containsRole = new ArrayList<>();
            for (Class<? extends Role> role : main.getGamemanager().getComposition()) {
                try {
                    Role roles = role.newInstance();
                    if(!containsRole.contains(roles.getRoleName())){
                        containsRole.add(roles.getRoleName());
                        if(roles.getCamp() != null && roles.getItem() != null) {
                            inv.addItem(roles.getItem().setLore("§7Camps : "+roles.getCamp().getCompoColor() + roles.getCamp().name(), "").setAmount(main.gameManager.countRolesInComposition(roles)).toItemStack());
                        } else {
                            inv.addItem(roles.getItem().setLore("").toItemStack());
                        }
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        },1);
    }
}
