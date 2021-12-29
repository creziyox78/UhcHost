package fr.lastril.uhchost.modes.naruto.v2.items;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.HuitPorteUser;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.GaiMaito;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.RockLee;
import fr.lastril.uhchost.modes.roles.Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.API.particles.DoubleCircleEffect;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SixPortesItem extends QuickItem {

	private final UhcHost main = UhcHost.getInstance();
	private NarutoV2Manager narutoV2Manager;

	public SixPortesItem() {
		super(Material.NETHER_STAR);
		super.setName("§2Six portes");
		super.onClick(onClick -> {
			narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();
			Player playerClick = onClick.getPlayer();
			PlayerManager joueur = UhcHost.getInstance().getPlayerManager(playerClick.getUniqueId());
			if(onClick.getAction() == Action.RIGHT_CLICK_BLOCK || onClick.getAction() == Action.RIGHT_CLICK_AIR){
				if (isRightRole(joueur.getRole())) {
					HuitPorteUser user = (HuitPorteUser) joueur.getRole();
					if(user.hasUsePorte()){
						playerClick.sendMessage(Messages.CANT_USE_MORE_POWER.getMessage());
						return;
					}
					if (!narutoV2Manager.isInSamehada(onClick.getPlayer().getUniqueId())) {
						if (joueur.getRoleCooldownSixPortes() <= 0) {
							if(playerClick.getMaxHealth() <= playerClick.getMaxHealth() - 2D) {
								playerClick.damage(100);
							} else {
								playerClick.setMaxHealth(playerClick.getMaxHealth() - 2D);
								if(playerClick.hasPotionEffect(PotionEffectType.SPEED))
									playerClick.removePotionEffect(PotionEffectType.SPEED);
								if(playerClick.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
									playerClick.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
								playerClick.addPotionEffect(
										new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * 5, 0, false, false));
								playerClick.addPotionEffect(
										new PotionEffect(PotionEffectType.SPEED, 20 * 60 * 5, 0, false, false));
								new DoubleCircleEffect(20 * 2 * 60, EnumParticle.VILLAGER_HAPPY).start(playerClick);
								joueur.setRoleCooldownSixPortes(10);
								if(joueur.getRole() instanceof NarutoV2Role){
									NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
									narutoRole.usePower(joueur);
									narutoRole.usePowerSpecific(joueur, super.getName());
								}
							}
						} else {
							playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownSixPortes()));
						}

					} else {
						playerClick
								.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "§cVous êtes sous l'effet de Samehada.");
					}
				}
			}

		});
	}
	
	private boolean isRightRole(Role role) {
		return role instanceof RockLee || role instanceof GaiMaito;
	}
	


}
