package fr.lastril.uhchost.modes.naruto.v2.roles.shinobi;

import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.Chakra;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ArmureFoudre;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.RoleListener;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class YondameRaikage extends Role implements NarutoV2Role, RoleListener {
	
	private int timer = 60*20;
	
	private boolean useFoudreArmor;
	
	private boolean hasUse;

	private double healthWhenKillerBeeDeath = 2D*2D;
	
	public YondameRaikage() {
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false), When.START);
		
	}
	
    @Override
    public void giveItems(Player player) {
    	main.getInventoryUtils().giveItemSafely(player, new ArmureFoudre().toItemStack());
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
	public void onPlayerDeath(Player player) {
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		if (joueur.hasRole()) {
			if(joueur.getRole() instanceof KillerBee){
				if (super.getPlayer() != null) {
					Player yondame = super.getPlayer();
					yondame.setMaxHealth(yondame.getMaxHealth() + (2D*2D));
					yondame.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Killer Bee est mort. Vous obtenez 2 coeurs supplémentaires.");
				}
			}
		}

	}

	@EventHandler
	public void onKillPlayer(PlayerKillEvent event){
		Player player = event.getDeadPlayer();
		PlayerManager joueur = main.getPlayerManager(player.getUniqueId());
		Player killer = event.getKiller();
		if (joueur.hasRole()) {
			if(joueur.getRole() instanceof KillerBee){
				if (super.getPlayer() != null) {
					Player yondame = super.getPlayer();
					yondame.sendMessage("Voici l'identité du tueur: " + killer.getName());
				}
			}
		}
	}

    @Override
	public QuickItem getItem() {
		return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(getCamp().getCompoColor() + getRoleName())
				.setTexture(
						"yJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWI4MDliZmMyMjdmYTlkNTIzNzMyNmM0YTdkZTFkMDA1YzY4ZjcxNDBhOTZmZjExN2VlOWJkNjJiZjhlYjM0NiJ9fX0=");
	}

    @Override
    public Camps getCamp() {
        return Camps.SHINOBI;
    }

    @Override
    public String getRoleName() {
        return "Yondaime Raikage";
    }

    @Override
    public String getDescription() {
		return main.getRoleDescription(this, this.getClass().getName(), "naruto.yml");
    }

    @Override
    public void onPlayerUsedPower(PlayerManager joueur) {

    }
    
    @Override
	public void afterRoles(Player player) {

	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public boolean isUseFoudreArmor() {
		return useFoudreArmor;
	}

	public void setUseFoudreArmor(boolean useFoudreArmor) {
		this.useFoudreArmor = useFoudreArmor;
	}

	public boolean isHasUse() {
		return hasUse;
	}

	public void setHasUse(boolean hasUse) {
		this.hasUse = hasUse;
	}

	@Override
	public Chakra getChakra() {
		return Chakra.RAITON;
	}
}
