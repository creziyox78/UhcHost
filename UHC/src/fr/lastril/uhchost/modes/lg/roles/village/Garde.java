package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.entity.Player;

public class Garde extends Role implements LGRole {
    @Override
    public String getSkullValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNmZDMyMDMyODY2ZThkYTI2ZWVmZGZlOGU5M2Y1MWQ1YTE0MjAzN2YxZGJhMjljNTJjMTYxZDc5YjNmOWYifX19";
    }

    @Override
    public void giveItems(Player player) {

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
    public ItemsCreator getItem() {
        return null;
    }

    @Override
    public Camps getCamp() {
        return null;
    }

    @Override
    public String getRoleName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
