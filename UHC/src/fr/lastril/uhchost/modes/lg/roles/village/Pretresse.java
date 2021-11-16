package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Pretresse extends Role implements LGRole {

    public String getSkullValue() {
        return null;
    }

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
    }

    @Override
    public void onNight(Player player) {
    }

    @Override
    public void onDay(Player player) {
    }

    @Override
    public void onNewEpisode(Player player) {

    }

    @Override
    public void onNewDay(Player player) {
    }

    @Override
    public String getRoleName() {
        return "Prêtresse";
    }

    @Override
    public String getDescription() {
        return main.getLGRoleDescription(this,this.getClass().getName());
    }


    @Override
    public QuickItem getItem() {
        return null;
        //return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI1NTU3YmQ1YTgyYjBjMjEwNDVlMzIyMGMzMzgwYWM5ZTBlZDQ2ZGM4M2I1ZjAwYTlkZjJlM2QzZTUwNDVhMiJ9fX0=");
    }

    @Override
    public void checkRunnable(Player player) {

    }

    @Override
    public Camps getCamp() {
        return Camps.VILLAGEOIS;
    }

}
