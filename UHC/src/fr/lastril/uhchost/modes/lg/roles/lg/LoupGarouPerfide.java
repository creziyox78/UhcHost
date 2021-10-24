package fr.lastril.uhchost.modes.lg.roles.lg;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.enums.WorldState;
import fr.lastril.uhchost.modes.lg.LoupGarouManager;
import fr.lastril.uhchost.modes.lg.roles.LGRole;
import fr.lastril.uhchost.modes.lg.roles.solo.LoupGarouBlanc;
import fr.lastril.uhchost.modes.lg.roles.village.PetiteFille;
import fr.lastril.uhchost.modes.roles.Camps;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.modes.roles.When;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.particles.ParticleEffect;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_8_R3.EnumParticle;

import java.util.ArrayList;
import java.util.List;

public class LoupGarouPerfide extends Role implements LGRole {

	private final List<PlayerManager> loupGarouList = new ArrayList<>();

	public LoupGarouPerfide() {
		super.addEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false), When.START);
		super.addEffect(new PotionEffect(PotionEffectType.SPEED, 20*60*1, 0, false, false), When.AT_KILL);
		super.addEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*60*1, 0, false, false), When.AT_KILL);
	}

	@Override
	public String getSkullValue() {
		return null;
	}

	@Override
	public String getRoleName() {
		return "Loup-Garou Perfide";
	}

	@Override
	public String getDescription() {
		return " ";
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
	public void afterRoles(Player player) {
		player.sendMessage(sendList());
	}

	@Override
	public String sendList() {
		if(main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){
			LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
			return loupGarouManager.sendLGList();
		}
		return null;
	}

	@Override
	public void onNewDay(Player player) {}

	@Override
	public void checkRunnable(Player player) {
		if(main.gameManager.getModes().getMode().getModeManager() instanceof LoupGarouManager){
			LoupGarouManager loupGarouManager = (LoupGarouManager) main.gameManager.getModes().getMode().getModeManager();
			if(main.gameManager.getWorldState() == WorldState.NIGHT) {
				if(isWithoutArmor(player)) {
					for(PlayerManager joueur : loupGarouManager.getJoueursWithRole(PetiteFille.class)) {
						if(joueur.getPlayer() != null) {
							Player pf = joueur.getPlayer();
							for (int i = 0; i < 10; i++) {
								ParticleEffect.playEffect(pf, EnumParticle.REDSTONE, player.getLocation());
							}
						}
					}
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20, 0, false, false));
					player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
				}else {
					if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) player.removePotionEffect(PotionEffectType.INVISIBILITY);
				}
			}else {
				if(player.hasPotionEffect(PotionEffectType.INVISIBILITY)) player.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}

	}
	
	public boolean isWithoutArmor(Player player) {
		EntityEquipment equipement = player.getEquipment();
		return equipement.getHelmet() == null && equipement.getChestplate() == null && equipement.getLeggings() == null && equipement.getBoots() == null;
	}

	@Override
	public ItemsCreator getItem() {
		return null;
		//return new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName("§4"+getRoleName()).setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY5MjQ4NmM5ZDZlNGJiY2UzZDVlYTRiYWFmMGNmN2JiZDQ5OTQ3OWQ4ZTM5YTM1NjBiYjZjOGM4YmYxYjZkYSJ9fX0=");
	}

	@Override
	public Camps getCamp() {
		return Camps.LOUP_GAROU;
	}

}
