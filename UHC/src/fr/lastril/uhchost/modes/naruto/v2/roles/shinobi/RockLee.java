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

public class RockLee extends Role implements NarutoV2Role, HuitPorteUser {

    private boolean usePorte;

    public RockLee(){
        super.addRoleToKnow(GaiMaito.class);
    }

    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new SixPortesItem().toItemStack());
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
				"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg4NDFkNWE1NzJlNTEyMGIyNTdmNWU3NzZhNGM0NDY0OWVkNjEyNzg3NDFjNTBhNTg5YzhjODY1NDEwZmQyNyJ9fX0=")
				.setName(getCamp().getCompoColor() + getRoleName());
	}

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Rock Lee";
    }

    @Override
    public String getDescription() {
        return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }
    
    @Override
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (joueur.hasRole()) {
			if(joueur.getRole() instanceof GaiMaito){
				if (super.getPlayer() != null) {
					Player rocklee = super.getPlayer();
					main.getInventoryUtils().giveItemSafely(rocklee, new HuitPortesItem().toItemStack());
				}
			}
		}
	}

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

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
