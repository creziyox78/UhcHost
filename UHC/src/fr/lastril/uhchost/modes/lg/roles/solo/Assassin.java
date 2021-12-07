package fr.lastril.uhchost.modes.lg.roles.solo;

import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.tools.API.items.Livre;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Assassin extends Role implements LGRole {

    public Assassin() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.DAY);
    }

    @Override
    public String getRoleName() {
        return "Assassin";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this,this.getClass().getName());
    }

    @Override
    public Camps getCamp() {
        return Camps.ASSASSIN;
    }

    @Override
    public void giveItems(Player player) {
        player.getInventory().addItem(new Livre(Enchantment.DAMAGE_ALL, 3).toItemStack());
        player.getInventory().addItem(new Livre(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack());
        player.getInventory().addItem(new Livre(Enchantment.DIG_SPEED, 3).toItemStack());
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
    public void checkRunnable(Player player) {
    }

    @Override
    public QuickItem getItem() {
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor()+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk1NzEyYjZlMWIzOGY5MmUyMWE1MmZiNzlhZjUzM2I3M2JiNWRkNWNiZGFmOTJlZTY0YjkzYWFhN2M0NjRkIn19fQ==");
    }

}
