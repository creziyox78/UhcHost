package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.atlantis.api.item.crafter.QuickItem;
import fr.maygo.uhc.modes.crafter.role.crafter.Camps;
import fr.maygo.uhc.modes.crafter.role.crafter.Role;
import fr.maygo.uhc.modes.crafter.role.crafter.When;
import fr.maygo.uhc.modes.naruto.v2.crafter.Chakra;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.NuesArdentesItem;
import fr.maygo.uhc.modes.naruto.v2.tasks.AsumaTask;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Asuma extends Role implements NarutoV2Role {

    public static final int FIND_POINTS = 2500;
    private int inoPoints, shikamaruPoints, chojiPoints;

    public Asuma() {
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false), When.START);
    }

    @Override
    public void giveItems(Player player) {
        main.getInventoryUtils().giveItemSafely(player, new QuickItem(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5, true).toItemStack());
        main.getInventoryUtils().giveItemSafely(player, new NuesArdentesItem(main).toItemStack());

        new AsumaTask(main, this).runTaskTimer(main, 0, 20);
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
        return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setTexture(
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAyZGUxNTczZTBlNDI5YTAwMTlmMGU4MjQ2MTZjY2UwYzhiNDI3NzRiMDBkZTcyYzZiYjczNmUyZjBjODI0OSJ9fX0=")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Asuma";
    }

    @Override
    public String getDescription() {
        return main.getUHCDatabase().getRoleDescription(this.getClass());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager PlayerManager) {

    }

    public int getInoPoints() {
        return inoPoints;
    }

    public void setInoPoints(int inoPoints) {
        this.inoPoints = inoPoints;
    }

    public int getShikamaruPoints() {
        return shikamaruPoints;
    }

    public void setShikamaruPoints(int shikamaruPoints) {
        this.shikamaruPoints = shikamaruPoints;
    }

    public int getChojiPoints() {
        return chojiPoints;
    }

    public void setChojiPoints(int chojiPoints) {
        this.chojiPoints = chojiPoints;
    }

    @Override
    public Chakra getChakra() {
        return Chakra.FUTON;
    }
}
