package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.izanami;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoalAuthor;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoalTarget;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;

public class IzanamiGoalGUI extends IQuickInventory {

    private final UhcHost main;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;

    public IzanamiGoalGUI(UhcHost main, GenjutsuItem.GenjutsuUser genjutsuUser){
        super("§cObjectif Izanami", 9 * 1);
        this.main = main;
        this.genjutsuUser = genjutsuUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("update", taskUpdate -> {
            IzanamiGoalAuthor firstGoal = genjutsuUser.getIzanamiGoal().getIzanamiGoalAuthorList().get(0);
            IzanamiGoalAuthor secondGoal = genjutsuUser.getIzanamiGoal().getIzanamiGoalAuthorList().get(1);
            IzanamiGoal izanamiGoal = genjutsuUser.getIzanamiGoal();
            IzanamiGoalTarget goal = genjutsuUser.getIzanamiGoal().getIzanamiGoalTargetList().get(0);
            inv.setItem(new QuickItem(Material.SKULL_ITEM, 1,SkullType.PLAYER.ordinal()).setName("§eVos objectifs").setSkullOwner(inv.getOwner().getName())
                    .setLore("",
                            firstGoal.getDescription() + " : " + izanamiGoal.getAuthorValueGoal(firstGoal),
                            secondGoal.getDescription() + " : " + izanamiGoal.getAuthorValueGoal(secondGoal))
                    .toItemStack(), 3);

            inv.setItem(new QuickItem(Material.SKULL_ITEM,1, SkullType.PLAYER.ordinal()).setName("§eObjectif de la cible").setSkullOwner(genjutsuUser.getIzanamiGoal().getTarget().getPlayerName())
                    .setLore("",
                            goal.getDescription() + " : " + izanamiGoal.getTargetValueGoal(goal))
                    .toItemStack(), 5);

        }, 1);
    }
}
