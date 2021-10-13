package fr.lastril.uhchost.modes.lg.roles.village;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.clickable_messages.ClickableMessage;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Soeur extends Role implements LGRole {
	
	private static final PotionEffect ressistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 0, false, false);
	
	public Soeur() {
		super.addRoleToKnow(Soeur.class);
	}

	@Override
	public String getSkullValue() {
		return null;
	}

	@Override
	public void giveItems(Player player) {}

	@Override
	public void onNight(Player player) {}

	@Override
	public void onDay(Player player) {}

	@Override
	public void onNewEpisode(Player player) {

	}

	@Override
	public void onNewDay(Player player) {}

	@Override
	public String getRoleName() {
		return "Soeur";
	}

	@Override
	public String getDescription() {
		return " Vous n'avez pas de pouvoir particulier.";
	}

	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§a"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkNTFmYjVhOTE4ZTMzYzA0YmIyMzIzNjE0N2QzNTU1OWRlMzJhNDQ4MTcwZjFhM2NjYWFlNmRjYTYzY2I2In19fQ==");
	}

	@Override
	public void checkRunnable(Player player) {
		for(PlayerManager joueur : UhcHost.getInstance().gameManager.getLoupGarouManager().getJoueursWithRole(Soeur.class)) {
			if(joueur.getPlayer() != player){
				if(joueur.getPlayer() != null) {
					Player soeur = joueur.getPlayer();
					if(player.getLocation().distance(soeur.getLocation()) <= 20) {
						player.addPotionEffect(ressistance);
						soeur.addPotionEffect(ressistance);
					}
				}
			}
		}
	}

	@Override
	public Camps getCamp() {
		return Camps.VILLAGEOIS;
	}

	@Override
	public void onPlayerDeathRealy(PlayerManager player, ItemStack[] items, ItemStack[] armors, Player killer, Location deathLocation) {
		PlayerManager joueur = UhcHost.getInstance().getPlayerManager(player.getUuid());
		if(joueur.getRole() instanceof Soeur) {
			if(joueur.getPlayer() != this.getPlayer()) {
				if(this.getPlayer() != null){
					Player soeur = this.getPlayer();
					soeur.sendMessage("Votre soeur ("+player.getPlayerName()+") est morte ! Vous pouvez décider de voir le pseudo du tueur ou son rôle.");
					new ClickableMessage(soeur, onClick -> {
						//VOIR PSEUDO
						soeur.sendMessage("Le tueur de votre sœur est "+killer.getName()+" !");
					}, "§eVoir le pseudo ");
					new ClickableMessage(soeur, onClick -> {
						//VOIR RÔLE
						soeur.sendMessage("Le rôle du tueur de votre sœur est "+UhcHost.getInstance().getPlayerManager(killer.getUniqueId()).getRole().getRoleName()+" !");
					}, "§eVoir le rôle ");
				}
			}
		}
	}
	
	
}
