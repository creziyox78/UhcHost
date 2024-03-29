package fr.lastril.uhchost.inventory.guis.modes.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LGCurrentCompositionGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();
    private Camps camps;

    public LGCurrentCompositionGui(Camps camps) {
        super("§bComposition", 9*6);
        this.camps = camps;
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("roles", taskUpdate -> {

            for (int i = 0; i < inv.getInventory().getSize() - 3; i++) {
                inv.setItem(new ItemStack(Material.AIR), i);
            }
            for(int i = 0; i < 18;i++){
                inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).toItemStack(), i);
            }
            List<Role> rolesInv = new ArrayList<>();
            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                int index = 8 + 9;
                for (Role roles : pl.getGamemanager().getRolesComposition()) {
                    if(roles.getCamp() == camps && !rolesInv.contains(roles)){
                        rolesInv.add(roles);
                        index++;
                        if(roles.getItem() == null){
                            inv.setItem(new QuickItem(Material.EMERALD,pl.gameManager.countRolesInComposition(roles)).setName(roles.getCamp().getCompoColor()
                                    +roles.getRoleName()).toItemStack(),index);
                        } else {
                            inv.setItem(roles.getItem().setAmount(pl.gameManager.countRolesInComposition(roles)).toItemStack(), index);
                        }
                    }
                }
            }

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.VILLAGEOIS.getCompoColor() + "Villageois")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTE5NTUxNjhlZjUzZjcxMjBjMDg5ZGFmZTNlNmU0MzdlOTUyNDA1NTVkOGMzYWNjZjk0NGQ2YzU2Yjc0MDQ3NSJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.VILLAGEOIS).size())
                    .toItemStack(), onClick -> camps = Camps.VILLAGEOIS, 3);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.LOUP_GAROU.getCompoColor() + "Loups-Garous")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0===")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.LOUP_GAROU).size())
                    .toItemStack(), onClick -> camps = Camps.LOUP_GAROU, 4);
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.NEUTRES.getCompoColor() + "Neutres et Solos")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk1NzEyYjZlMWIzOGY5MmUyMWE1MmZiNzlhZjUzM2I3M2JiNWRkNWNiZGFmOTJlZTY0YjkzYWFhN2M0NjRkIn19fQ==")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.NEUTRES).size())
                    .toItemStack(), onClick -> camps = Camps.NEUTRES, 5);


            inv.setItem(new QuickItem(Material.COMPASS)
                    .setName("§fRôles totaux actifs: " + pl.getGamemanager().getComposition().size())
                    .toItemStack(), 13);

        });


    }
}
