package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ParcheminItem;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TenTen extends Role implements NarutoV2Role {
    @Override
    public void giveItems(Player player) {
    	QuickItem epee = new QuickItem(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 4, true);
		QuickItem arc = new QuickItem(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3, true)
				.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
		main.getInventoryUtils().giveItemSafely(player, new ItemStack(Material.ARROW, 32));
		main.getInventoryUtils().giveItemSafely(player, arc.toItemStack());
		main.getInventoryUtils().giveItemSafely(player, epee.toItemStack());
		main.getInventoryUtils().giveItemSafely(player, new ParcheminItem().toItemStack());
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
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNhMGM1NTZmY2RlYTlkNjc0NDE3ZDY2MDZkMTMxMzU1OTRmM2Q0YTVkZWIzOWRjYjVhMjc0NWMzNjlkMDIzMyJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "TenTen";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName());
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

	@Override
	public Chakra getChakra() {
		return null;
	}
}
