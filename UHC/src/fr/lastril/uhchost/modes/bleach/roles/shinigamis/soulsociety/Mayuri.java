
package fr.lastril.uhchost.modes.bleach.roles.shinigamis.soulsociety;

import fr.lastril.uhchost.modes.bleach.items.Shikai;
import fr.lastril.uhchost.modes.bleach.roles.ShinigamiRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.entity.Player;

public class Mayuri extends Role implements ShinigamiRole {

    private boolean inShikai;

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new Shikai(main).toItemStack());
    }

    @Override
    protected void onNight(Player player) {

    }

    @Override
    protected void onDay(Player player) {

    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {

    }

    @Override
    public QuickItem getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINIGAMIS;
    }

    @Override
    public String getRoleName() {
        return "Mayuri";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "bleach.yml");
    }

    public void setInShikai(boolean inShikai) {
        this.inShikai = inShikai;
    }
}
