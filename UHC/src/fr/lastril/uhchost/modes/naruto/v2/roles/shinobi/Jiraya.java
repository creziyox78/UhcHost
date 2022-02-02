package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.SenjutsuItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jiraya extends Role implements NarutoV2Role, SenjutsuItem.SenjutsuUser {

    public Jiraya() {
        super.addEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false), When.START);
        super.addEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false), When.START);
    }


    @Override
    public void giveItems(Player player) {
    	 main.getInventoryUtils().giveItemSafely(player, new SenjutsuItem(main).toItemStack());
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
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ0MmMzY2VjYzUwZjFiNTE2ZDdiM2YyOTY5ZjZjYjk3MjFkNGQ5OGUzYzNmYTY0OTI1YTNmMjlkYjA0NGViIn19fQ==")
                .setName(getCamp().getCompoColor() + getRoleName());
    }

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Jiraya";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }


	@Override
	public Chakra getChakra() {
		return Chakra.KATON;
	}
}
