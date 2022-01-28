package fr.lastril.uhchost.inventory.guis.modes.naruto;

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

public class NarutoCurrentCompositionGui extends IQuickInventory {

    private final UhcHost pl = UhcHost.getInstance();
    private final List<Camps> camps = new ArrayList<>();

    public NarutoCurrentCompositionGui() {
        super("§bComposition", 9*6);
        camps.add(Camps.SHINOBI);
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

            if(pl.gameManager.getModes().getMode() instanceof RoleMode<?>){
                int index = 8 + 9;
                for (Role roles : pl.getGamemanager().getRolesComposition()) {
                    if(camps.contains(roles.getCamp())){
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
                    .setName(Camps.SHINOBI.getCompoColor() + "Shinobis")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA2ZTM0YzFjOTRjN2I4MjljNTlkMDFjNzQ1M2Q1ZjNlODI1OWYzODljMmFjYTJmYTMxNGRjYTQwODY5M2NlIn19fQ==")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.SHINOBI).size())
                    .toItemStack(), onClick -> {
                camps.clear();
                camps.add(Camps.SHINOBI);
            }, 2);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.AKATSUKI.getCompoColor() + "Akatsuki")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjkyMjY0NmE4OTQ1YjVjNDAwZTkwNDZjN2JhMjA4MGZjNmQ0N2ExNjUxMjEyNmI4OWJmNzc3ZDg0MjllZTU1NiJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.AKATSUKI).size())
                    .toItemStack(), onClick -> {
                camps.clear();
                camps.add(Camps.AKATSUKI);
            }, 3);
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.OROCHIMARU.getCompoColor() + "Orochimaru")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzQ2YjgzZmU3MTBlOTg3NzdkMjlhZTgwNGVmNmQzYjhkMjRiYzVjNTlmZDM3YjViYTA4NDc1YmQ3Njc4ZWQwYSJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.OROCHIMARU).size())
                    .toItemStack(), onClick -> {
                camps.clear();
                camps.add(Camps.OROCHIMARU);
            }, 4);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.TAKA.getCompoColor() + "Taka")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFlYmNhMzcyOWJhOTgwOTM1OTg4OGNiNDY4MTM1YmI0OTNjOGQxOGM5OGI5NWMxNTdlZTk5MDQyOWExMCJ9fX0=")
                    .setLore("§fActifs: " + pl.getGamemanager().getRolesActivatedInCamps(Camps.TAKA).size())
                    .toItemStack(), onClick -> {
                camps.clear();
                camps.add(Camps.TAKA);
            }, 5);

            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(Camps.ZABUZA_HAKU.getCompoColor() + "Duos et Solos")
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODcwYzQzNWI1NDgwNmIyZDU4NzhlMDk3NWExM2ZiOTU1ZGFiMmZiNjg4ZjUwYjAxMTM1NzY0OGY4N2E0Y2QxIn19fQ==")
                    .setLore("§fActifs: " + (pl.getGamemanager().getRolesActivatedInCamps(Camps.ZABUZA_HAKU).size()
                            + pl.getGamemanager().getRolesActivatedInCamps(Camps.DANZO).size()
                            + pl.getGamemanager().getRolesActivatedInCamps(Camps.JUBI).size()
                    +pl.getGamemanager().getRolesActivatedInCamps(Camps.GAARA).size()))
                    .toItemStack(), onClick -> {
                camps.clear();
                camps.add(Camps.GAARA);
                camps.add(Camps.JUBI);
                camps.add(Camps.DANZO);
                camps.add(Camps.ZABUZA_HAKU);
            }, 6);


            inv.setItem(new QuickItem(Material.COMPASS)
                    .setName("§fRôles totaux actifs: " + pl.getGamemanager().getComposition().size())
                    .toItemStack(), 13);

        });


    }
}
