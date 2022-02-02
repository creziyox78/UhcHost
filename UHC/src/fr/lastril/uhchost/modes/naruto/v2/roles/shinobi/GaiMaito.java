package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;


import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.HuitPortesItem;
import fr.lastril.uhchost.modes.naruto.v2.items.SixPortesItem;
import fr.lastril.uhchost.modes.naruto.v2.items.TroisPortesItem;
import fr.lastril.uhchost.modes.naruto.v2.roles.HuitPorteUser;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

public class GaiMaito extends Role implements NarutoV2Role, HuitPorteUser {

    public GaiMaito(){
        super.addRoleToKnow(RockLee.class);
    }

    private boolean inGaiNuit, mustDie = true, usePorte;

    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new SixPortesItem().toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new HuitPortesItem().toItemStack());
    	main.getInventoryUtils().giveItemSafely(player, new TroisPortesItem().toItemStack());
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
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWQ0YzdjZmY5MDYyNjVjMTUwOThmMjUyMjljOWYwMDBhODZmMTVlNjlmZjE3Mjg3YzFmNmI2YmFhN2Y0N2ZhZCJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Gaï Maito";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }

    public boolean isMustDie() {
        return mustDie;
    }

    public void setMustDie(boolean mustDie) {
        this.mustDie = mustDie;
    }

    public boolean isInGaiNuit() {
        return inGaiNuit;
    }

    public void setInGaiNuit(boolean inGaiNuit) {
        this.inGaiNuit = inGaiNuit;
    }

	@Override
	public Chakra getChakra() {
		return null;
	}

    @Override
    public boolean hasUsePorte() {
        return usePorte;
    }

    @Override
    public void setHasUsePorte(boolean hasUsePorte) {
        this.usePorte = hasUsePorte;
    }
}
