package fr.lastril.uhchost.inventory.guis.modes.ds;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.ModeConfig;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleMode;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class DSCompositionGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();
    private Camps camps;

    public DSCompositionGui(Camps camps) {
        super("§bComposition", 9*6);
        this.camps = camps;
    }


    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("roles", taskUpdate -> {
            for (int i = 0; i < inv.getInventory().getSize() - 3; i++) {
                inv.setItem(new ItemStack(Material.AIR), i);
            }

            for(int i = 0; i < 19;i++){
                inv.setItem(new QuickItem(Material.STAINED_GLASS_PANE, 1, (byte)1).toItemStack(), i);
            }

            inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), onClick->{
                if(UhcHost.getInstance().gameManager.getModes().getMode() instanceof ModeConfig){
                    ModeConfig modeConfig = (ModeConfig) UhcHost.getInstance().gameManager.getModes().getMode();
                    modeConfig.getGui().open(onClick.getPlayer());
                }
            },inv.getInventory().getSize() - 1);

            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                RoleMode<?> mode = (RoleMode<?>) pl.getGamemanager().getModes().getMode();
                int index = 8 + 9;
                for (Role roles : mode.getRoles()) {
                    if(roles.getCamp() == camps){
                        index++;
                        if(roles.getItem() == null){
                            inv.setItem(new QuickItem(Material.EMERALD,pl.gameManager.countRolesInComposition(roles)).setName(roles.getCamp().getCompoColor()
                                    +roles.getRoleName()).toItemStack(), onClick -> {
                                if(onClick.getClickType() == ClickType.LEFT){
                                    pl.gameManager.addRoleToComposition(roles);
                                } else if(onClick.getClickType() == ClickType.RIGHT){
                                    pl.gameManager.removeRoleToComposition(roles);
                                }
                            },index);
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
            }

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.DEMONS.getCompoColor() + "Démons")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU3YmJlMjczODNhMzNjMmZmM2IzYjAwZjQ2MzQ2ZmNjZWM4Zjg0YmE2NDExNGZjN2E3ODAyNTUzN2Q3OGNlMSJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.DEMONS).size())
                    .toItemStack(), onClick -> camps = Camps.DEMONS, 3);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.SLAYER.getCompoColor() + "Slayers")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTA5MzIzMmFlYTQ5NjAwYjUwOTJmYzE4MmUwZDVjZTg5OTlmNzgwNDVkZDdiZGEyM2M2NWNjYTZmY2Y1Y2Y2MCJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.SLAYER).size())
                    .toItemStack(), onClick -> camps = Camps.SLAYER, 4);
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.NEUTRES.getCompoColor() + "Solos")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk1NzEyYjZlMWIzOGY5MmUyMWE1MmZiNzlhZjUzM2I3M2JiNWRkNWNiZGFmOTJlZTY0YjkzYWFhN2M0NjRkIn19fQ==")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.NEUTRES).size())
                    .toItemStack(), onClick -> camps = Camps.NEUTRES, 5);


            inv.setItem(new QuickItem(Material.COMPASS)
                    .setName("§fRôles totaux actifs: " + pl.getGamemanager().getComposition().size())
                    .toItemStack(), 13);

        });


    }
}
